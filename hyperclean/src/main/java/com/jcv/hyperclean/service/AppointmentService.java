package com.jcv.hyperclean.service;

import com.jcv.hyperclean.cache.RedisItemCache;
import com.jcv.hyperclean.cache.RedisListCache;
import com.jcv.hyperclean.dto.AppointmentDTO;
import com.jcv.hyperclean.dto.request.AppointmentRequestDTO;
import com.jcv.hyperclean.enums.AppointmentStatus;
import com.jcv.hyperclean.exception.HCInvalidDateTimeFormat;
import com.jcv.hyperclean.exception.HCNotFoundException;
import com.jcv.hyperclean.exception.HCValidationFailedException;
import com.jcv.hyperclean.exception.HCVehicleTimeSlotOccupiedException;
import com.jcv.hyperclean.model.Appointment;
import com.jcv.hyperclean.model.Vehicle;
import com.jcv.hyperclean.repository.AppointmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService extends CacheableService<Appointment> {
    private final AppointmentRepository appointmentRepository;
    private final VehicleService vehicleService;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, VehicleService vehicleService, RedisItemCache<Appointment> cache, RedisListCache<Appointment> listCache) {
        super(cache, listCache);
        this.appointmentRepository = appointmentRepository;
        this.vehicleService = vehicleService;
    }

    @Transactional
    public Appointment save(Appointment appointment) {
        invalidateCache(appointment.getId());
        appointment = appointmentRepository.save(appointment);
        putInCache(appointment.getId(), appointment);
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment create(AppointmentRequestDTO requestDTO) throws HCValidationFailedException, HCInvalidDateTimeFormat, HCVehicleTimeSlotOccupiedException {
        Vehicle vehicle = vehicleService.findById(requestDTO.getVehicleId());
        Appointment appointment = Appointment.of(requestDTO);
        appointment.setVehicle(vehicle);

        validateVehicleAvailableForAppointment(appointment);
        return save(appointment);
    }

    @Transactional(readOnly = true)
    public Appointment findById(Long id) throws HCNotFoundException {
        try {
            return findBy(id, appointmentRepository::findById);
        } catch (EntityNotFoundException e) {
            String errorMsg = String.format("Could not find an appointment with id: %s ", id);
            throw new HCNotFoundException(errorMsg);
        }
    }

    @Transactional(readOnly = true)
    public List<Appointment> findByVehicleId(Long vehicleId) {
        return findListBy(vehicleId, appointmentRepository::findByVehicleId);
    }

    @Transactional
    public AppointmentDTO markAsInProgress(Long id) throws HCValidationFailedException {
        Appointment appointment = findById(id);
        if (!appointment.isPending()) {
            throw new HCValidationFailedException(appointment, "Appointment is not in pending state");
        }

        return updateStatusAndSave(appointment, AppointmentStatus.IN_PROGRESS);
    }

    @Transactional
    public AppointmentDTO markAsFinished(Long id) throws HCValidationFailedException {
        Appointment appointment = findById(id);
        if (!appointment.hasFinished()) {
            throw new HCValidationFailedException(appointment, "Appointment has not finished");
        }

        return updateStatusAndSave(appointment, AppointmentStatus.FINISHED);
    }

    @Transactional
    public void markAsPaid(Appointment appointment) throws HCValidationFailedException {
        validateApplicableForPayment(appointment);

        updateStatusAndSave(appointment, AppointmentStatus.PAID);
    }

    private AppointmentDTO updateStatusAndSave(Appointment appointment, AppointmentStatus status) {
        appointment.setStatus(status);
        appointmentRepository.save(appointment);
        invalidateListCache(appointment.getVehicle().getId());
        invalidateCache(appointment.getId());
        return AppointmentDTO.from(appointment);
    }

    private void validateApplicableForPayment(Appointment appointment) throws HCValidationFailedException {
        if (appointment.wasPaid()) {
            throw new HCValidationFailedException(appointment, "Appointment already paid");
        }

        if (!appointment.isApplicableForPayment()) {
            throw new HCValidationFailedException(appointment, "Appointment is not applicable for payment");
        }
    }

    /**
     * Verifies if the appointment will collide with another scheduled appointments
     *
     * @param appointment the new appointment
     * @throws HCVehicleTimeSlotOccupiedException if the requested time slot has been claimed by other appointment for the same vehicle
     */
    private void validateVehicleAvailableForAppointment(Appointment appointment) throws HCVehicleTimeSlotOccupiedException {
        Vehicle vehicle = appointment.getVehicle();
        List<Appointment> appointments = safeFindListBy(vehicle.getId(), appointmentRepository::findByVehicleId);
        for (Appointment scheduled : appointments) {
            checkIfDateCollidesWithAppointment(scheduled, appointment);
        }
    }

    /**
     * Checks if the expected date will collide with a saved appointment
     *
     * @param scheduled      the saved appointment
     * @param newAppointment the appointment to program
     * @throws HCVehicleTimeSlotOccupiedException if the time slot will be occupied by the already scheduled appointment
     */
    private void checkIfDateCollidesWithAppointment(Appointment scheduled, Appointment newAppointment) throws HCVehicleTimeSlotOccupiedException {
        boolean isCompleted = List.of(AppointmentStatus.FINISHED, AppointmentStatus.PAID).contains(scheduled.getStatus());
        if (isCompleted) {
            return;
        }

        LocalDateTime appointmentDate = newAppointment.getAppointmentDate();
        LocalDateTime scheduledDate = scheduled.getAppointmentDate();
        LocalDateTime timeOfFinish = scheduledDate.plusMinutes(scheduled.getCleaningTime());
        boolean isOccupied = appointmentDate.isBefore(timeOfFinish) && appointmentDate.isAfter(scheduledDate);
        if (isOccupied) {
            String errorMsg = String.format("The vehicle already has a scheduled appointment, between %s and %s", scheduledDate, timeOfFinish);
            throw new HCVehicleTimeSlotOccupiedException(AppointmentDTO.from(newAppointment), errorMsg);
        }
    }
}
