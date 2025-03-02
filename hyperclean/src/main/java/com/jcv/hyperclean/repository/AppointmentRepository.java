package com.jcv.hyperclean.repository;

import com.jcv.hyperclean.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByVehicleId(Long vehicleId);
}
