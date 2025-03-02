package com.jcv.hyperclean.service;

import com.jcv.hyperclean.dto.AppointmentDTO;
import com.jcv.hyperclean.dto.request.AppointmentRequestDTO;
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
}
