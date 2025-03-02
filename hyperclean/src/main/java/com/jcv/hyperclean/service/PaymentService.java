package com.jcv.hyperclean.service;

import com.jcv.hyperclean.dto.PaymentDTO;
import com.jcv.hyperclean.dto.request.PaymentRequestDTO;
import com.jcv.hyperclean.enums.PaymentType;
import com.jcv.hyperclean.model.Appointment;
import com.jcv.hyperclean.model.Payment;
import com.jcv.hyperclean.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final AppointmentService appointmentService;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, AppointmentService appointmentService) {
        this.paymentRepository = paymentRepository;
        this.appointmentService = appointmentService;
    }

    @Transactional
    public PaymentDTO create(PaymentRequestDTO requestDTO) {
        if (!requestDTO.getType().equals(PaymentType.CASH)) {
            throw new IllegalArgumentException("Currently we only support payment by cash");
        }

        Appointment appointment = appointmentService.findById(requestDTO.getAppointmentId());

        Payment payment = Payment.of(requestDTO);
        payment.setAppointment(appointment);
        validatePayment(payment);

        appointmentService.markAsPaid(appointment);
        return PaymentDTO.from(paymentRepository.save(payment));
    }

    @Transactional(readOnly = true)
    public PaymentDTO findById(Long id) {
        return PaymentDTO.from(paymentRepository.getReferenceById(id));
    }

    @Transactional(readOnly = true)
    public PaymentDTO findByAppointmentId(Long appointmentId) {
        return PaymentDTO.from(paymentRepository.findByAppointmentId(appointmentId));
    }

    private void validatePayment(Payment payment) {
        Appointment appointment = payment.getAppointment();
        Double costOfCleaning = appointment.getCostOfCleaning();

        if (costOfCleaning > payment.getAmount()) {
            throw new IllegalArgumentException("Payment amount is less than the cost of cleaning");
        }
    }
}
