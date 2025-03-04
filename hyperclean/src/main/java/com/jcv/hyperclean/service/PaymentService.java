package com.jcv.hyperclean.service;

import com.jcv.hyperclean.cache.RedisItemCache;
import com.jcv.hyperclean.cache.RedisListCache;
import com.jcv.hyperclean.dto.PaymentDTO;
import com.jcv.hyperclean.dto.request.PaymentRequestDTO;
import com.jcv.hyperclean.enums.PaymentType;
import com.jcv.hyperclean.model.Appointment;
import com.jcv.hyperclean.model.Payment;
import com.jcv.hyperclean.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

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
    public PaymentDTO create(PaymentRequestDTO requestDTO) {
        if (!requestDTO.getType().equals(PaymentType.CASH)) {
            throw new IllegalArgumentException("Currently we only support payment by cash");
        }

        Appointment appointment = appointmentService.findById(requestDTO.getAppointmentId());

        Payment payment = Payment.of(requestDTO);
        payment.setAppointment(appointment);
        validatePayment(payment);

        appointmentService.markAsPaid(appointment);

        putInCache(String.valueOf(payment.getId()), payment);
        return PaymentDTO.from(save(payment));
    }

    @Transactional(readOnly = true)
    public PaymentDTO findById(Long id) {
        String cacheKey = String.valueOf(id);
        return findPaymentByKey(cacheKey, id, paymentRepository::getReferenceById);
    }

    @Transactional(readOnly = true)
    public PaymentDTO findByAppointmentId(Long appointmentId) {
        String cacheKey = String.valueOf(appointmentId);
        return findPaymentByKey(cacheKey, appointmentId, paymentRepository::getReferenceById);
    }

    private PaymentDTO findPaymentByKey(String cacheKey, Long id, Function<Long, Payment> repositoryMethod) {
        Optional<Payment> cachedPayment = getCached(cacheKey);
        if (cachedPayment.isPresent()) {
            return PaymentDTO.from(cachedPayment.get());
        }

        Payment payment = repositoryMethod.apply(id);
        putInCache(cacheKey, payment);

        return PaymentDTO.from(payment);
    }

    private void validatePayment(Payment payment) {
        Appointment appointment = payment.getAppointment();
        Double costOfCleaning = appointment.getCostOfCleaning();

        if (costOfCleaning > payment.getAmount()) {
            throw new IllegalArgumentException("Payment amount is less than the cost of cleaning");
        }
    }
}
