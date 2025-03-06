package com.jcv.hyperclean.service;

import com.jcv.hyperclean.cache.RedisItemCache;
import com.jcv.hyperclean.cache.RedisListCache;
import com.jcv.hyperclean.dto.request.PaymentRequestDTO;
import com.jcv.hyperclean.enums.PaymentType;
import com.jcv.hyperclean.exception.HCInvalidDateTimeFormat;
import com.jcv.hyperclean.exception.HCNotFoundException;
import com.jcv.hyperclean.exception.HCValidationFailedException;
import com.jcv.hyperclean.model.Appointment;
import com.jcv.hyperclean.model.Payment;
import com.jcv.hyperclean.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService extends CacheableService<Payment> {
    private final PaymentRepository paymentRepository;
    private final AppointmentService appointmentService;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, AppointmentService appointmentService, RedisItemCache<Payment> paymentCache, RedisListCache<Payment> paymentListCache) {
        super(paymentCache, paymentListCache);
        this.paymentRepository = paymentRepository;
        this.appointmentService = appointmentService;
    }

    @Transactional
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment create(PaymentRequestDTO requestDTO) throws HCValidationFailedException, HCInvalidDateTimeFormat {
        if (!requestDTO.getType().equals(PaymentType.CASH)) {
            throw new HCValidationFailedException(requestDTO, "Currently we only support payment by cash");
        }

        Appointment appointment = appointmentService.findById(requestDTO.getAppointmentId());

        Payment payment = Payment.of(requestDTO);
        payment.setAppointment(appointment);
        validatePayment(payment);

        appointmentService.markAsPaid(appointment);

        putInCache(payment.getId(), payment);
        return save(payment);
    }

    @Transactional(readOnly = true)
    public Payment findById(Long id) {
        try {
            return findBy(id, paymentRepository::findById);
        } catch (EntityNotFoundException e) {
            String errorMsg = String.format("Could not find a payment with id: %s ", id);
            throw new HCNotFoundException(errorMsg);
        }
    }

    @Transactional(readOnly = true)
    public Payment findByAppointmentId(Long appointmentId) {
        try {
            return findBy(appointmentId, paymentRepository::findByAppointmentId);
        } catch (EntityNotFoundException e) {
            String errorMsg = String.format("Could not find a payment with  appointment id: %s ", appointmentId);
            throw new HCNotFoundException(errorMsg);
        }
    }


    private void validatePayment(Payment payment) throws HCValidationFailedException {
        Appointment appointment = payment.getAppointment();
        Double costOfCleaning = appointment.getCostOfCleaning();

        if (costOfCleaning > payment.getAmount()) {
            throw new HCValidationFailedException(payment, "Payment amount is less than the cost of cleaning");
        }
    }
}
