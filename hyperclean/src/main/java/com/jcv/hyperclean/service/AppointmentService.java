package com.jcv.hyperclean.service;

import com.jcv.hyperclean.cache.RedisItemCache;
import com.jcv.hyperclean.cache.RedisListCache;
import com.jcv.hyperclean.dto.AppointmentDTO;
import com.jcv.hyperclean.dto.request.AppointmentRequestDTO;
import com.jcv.hyperclean.enums.AppointmentStatus;
import com.jcv.hyperclean.exception.HCInvalidDateTimeFormat;
import com.jcv.hyperclean.exception.HCValidationFailedException;
import com.jcv.hyperclean.exception.HCVehicleTimeSlotOccupiedException;
import com.jcv.hyperclean.model.Appointment;
import com.jcv.hyperclean.model.Vehicle;
import com.jcv.hyperclean.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.jcv.hyperclean.util.ListUtils.filterList;

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
        if (appointment.getAppointmentDate().isBefore(LocalDateTime.now())) {
            throw new HCValidationFailedException("You can't make an appointment using previous dates");
        }

        appointment.setVehicle(vehicle);

        validateVehicleAvailableForAppointment(appointment);
        return save(appointment);
    }

    @Transactional(readOnly = true)
    public Appointment findById(Long id) {
        return findBy(id, appointmentRepository::findById);
    }

    @Transactional(readOnly = true)
    public List<Appointment> findByVehicleId(Long vehicleId) {
        return findListBy(vehicleId, appointmentRepository::findByVehicleId);
    }

    @Transactional
    public AppointmentDTO markAsInProgress(Long id) throws HCValidationFailedException {
        Appointment appointment = findById(id);
        if (!appointment.isPending()) {
            throw new HCValidationFailedException("Appointment is not in pending state");
        }

        return updateStatusAndSave(appointment, AppointmentStatus.IN_PROGRESS);
    }

    @Transactional
    public AppointmentDTO markAsFinished(Long id) throws HCValidationFailedException {
        Appointment appointment = findById(id);
        if (!appointment.hasFinished()) {
            throw new HCValidationFailedException("Appointment has not finished");
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
            throw new HCValidationFailedException("Appointment already paid");
        }

        if (!appointment.isApplicableForPayment()) {
            throw new HCValidationFailedException("Appointment is not applicable for payment");
        }
    }

    /**
     * Verifies if the appointment will collide with another scheduled appointments
     * @param appointment the new appointment
     * @throws HCVehicleTimeSlotOccupiedException if the requested time slot has been claimed by other appointment for the same vehicle
     */
    private void validateVehicleAvailableForAppointment(Appointment appointment) throws HCVehicleTimeSlotOccupiedException {
        Vehicle vehicle = appointment.getVehicle();
        List<Appointment> appointments = findListBy(vehicle.getId(), appointmentRepository::findByVehicleId);
        appointments = filterList(appointments, scheduled -> isAvailableForAppointment(scheduled, appointment.getAppointmentDate()));

        if (!appointments.isEmpty()) {
            throw new HCVehicleTimeSlotOccupiedException();
        }
    }

    /**
     * Checks if the expected date will collide with a saved appointment
     * @param appointment the saved appointment
     * @param expectedDate the date to program the new appointment
     * @return if the vehicle will be available for the new appointment
     */
    private boolean isAvailableForAppointment(Appointment appointment, LocalDateTime expectedDate) {
        boolean isCompleted = List.of(AppointmentStatus.FINISHED, AppointmentStatus.PAID).contains(appointment.getStatus());
        if (isCompleted) {
            return true;
        }

        LocalDateTime appointmentDate = appointment.getAppointmentDate();
        LocalDateTime timeOfFinish = appointmentDate.plusMinutes(appointment.getCleaningTime());
        boolean isOccupied = expectedDate.isBefore(timeOfFinish) && expectedDate.isAfter(appointmentDate);
        return !isOccupied;
    }
}
