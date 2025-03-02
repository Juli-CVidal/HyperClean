package com.jcv.hyperclean.service;

import com.jcv.hyperclean.dto.AppointmentDTO;
import com.jcv.hyperclean.dto.request.AppointmentRequestDTO;
import com.jcv.hyperclean.enums.AppointmentStatus;
import com.jcv.hyperclean.model.Appointment;
import com.jcv.hyperclean.model.Vehicle;
import com.jcv.hyperclean.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.jcv.hyperclean.util.ListUtils.mapList;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final VehicleService vehicleService;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, VehicleService vehicleService) {
        this.appointmentRepository = appointmentRepository;
        this.vehicleService = vehicleService;
    }

    @Transactional
    public Appointment create(AppointmentRequestDTO requestDTO) {
        Vehicle vehicle = vehicleService.findById(requestDTO.getVehicleId());
        Appointment appointment = Appointment.of(requestDTO);
        appointment.setVehicle(vehicle);

        if (appointment.getStatus() == null) {
            appointment.setStatus(AppointmentStatus.PENDING);
        }

        return appointmentRepository.save(appointment);
    }

    @Transactional(readOnly = true)
    public Appointment findById(Long id) {
        return appointmentRepository.getReferenceById(id);
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> findByVehicleId(Long vehicleId) {
        return mapList(appointmentRepository.findByVehicleId(vehicleId), AppointmentDTO::from);
    }

    @Transactional
    public AppointmentDTO markAsInProgress(Long id) {
        Appointment appointment = findById(id);

        if (!appointment.isPending()) {
            throw new IllegalArgumentException("Appointment is not in pending state");
        }

        appointment.setStatus(AppointmentStatus.IN_PROGRESS);
        appointmentRepository.save(appointment);
        return AppointmentDTO.from(appointment);
    }

    @Transactional
    public AppointmentDTO markAsFinished(Long id) {
        Appointment appointment = findById(id);

        if (!appointment.hasFinished()) {
            throw new IllegalStateException("Appointment has not finished");
        }

        return updateStatusAndSave(appointment, AppointmentStatus.FINISHED);
    }

    @Transactional
    public void markAsPaid(Appointment appointment) {
        validateApplicableForPayment(appointment);

        appointment.setStatus(AppointmentStatus.PAID);
        appointmentRepository.save(appointment);
    }

    private AppointmentDTO updateStatusAndSave(Appointment appointment, AppointmentStatus status) {
        appointment.setStatus(status);
        appointmentRepository.save(appointment);
        return AppointmentDTO.from(appointment);
    }

    private void validateApplicableForPayment(Appointment appointment) {
        if (appointment.wasPaid()) {
            throw new IllegalArgumentException("Appointment already paid");
        }

        if (!appointment.isApplicableForPayment()) {
            throw new IllegalArgumentException("Appointment is not applicable for payment");
        }
    }
}
