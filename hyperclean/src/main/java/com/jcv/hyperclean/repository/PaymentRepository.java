package com.jcv.hyperclean.repository;

import com.jcv.hyperclean.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByAppointmentId(Long appointmentId);
}
