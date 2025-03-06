package com.jcv.hyperclean.service;

import com.jcv.hyperclean.dto.request.PaymentRequestDTO;
import com.jcv.hyperclean.enums.AppointmentStatus;
import com.jcv.hyperclean.enums.PaymentType;
import com.jcv.hyperclean.exception.HCNotFoundException;
import com.jcv.hyperclean.exception.HCValidationFailedException;
import com.jcv.hyperclean.model.Appointment;
import com.jcv.hyperclean.model.Payment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class PaymentServiceTestCase extends BaseServiceTest {
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        appointment = createAppointment(LocalDateTime.now().minusDays(1));
    }

    @Test
    void testCreatePayment() {
        Double amount = 0D;
        LocalDateTime date = LocalDateTime.now();
        PaymentType type = PaymentType.DEBIT;

        PaymentRequestDTO requestDTO = new PaymentRequestDTO(amount, date, type, appointment.getId());

        // Currently the only supported method is CASH
        Assertions.assertThrows(HCValidationFailedException.class, () -> paymentService.create(requestDTO));
        requestDTO.setType(PaymentType.CASH);

        // The amount to pay is lesser than the cost of the appointment (appointment.getCostOfCleaning())
        Assertions.assertThrows(HCValidationFailedException.class, () -> paymentService.create(requestDTO));
        requestDTO.setAmount(appointment.getCostOfCleaning());

        // The appointment is not marked as finished
        Assertions.assertThrows(HCValidationFailedException .class, () -> paymentService.create(requestDTO));
        markAppointmentAsFinished(appointment);

        Payment payment = Assertions.assertDoesNotThrow(() -> paymentService.create(requestDTO));
        Assertions.assertNotNull(payment.getId());
        Assertions.assertTrue(payment.getAppointment().wasPaid());
    }

    @Test
    void testBaseCreation() {
        // Base method - no parameters
        Payment payment = createPayment();
        assertHasFields(payment);

        // Only appointment
        payment = createPayment(appointment);
        Assertions.assertEquals(appointment.getId(), payment.getAppointment().getId());

        // Complete method
        Double amount = 20000D;
        LocalDateTime date = LocalDateTime.now();
        appointment = createAppointment(LocalDateTime.now().minusDays(1));
        PaymentType type = PaymentType.CASH;
        payment = createPayment(amount, date, appointment, type);
        assertFields(payment,amount, date, appointment, type);
    }

    @Test
    void testFindById() {
        Payment payment = createPayment(createAppointment(LocalDateTime.now().minusDays(1)));
        Assertions.assertNotNull(paymentService.findById(payment.getId()));
    }

    @Test
    void testNotFoundById() {
        Assertions.assertThrows(HCNotFoundException.class, () -> paymentService.findById(1L));
    }

    @Test
    void testFindByAppointmentId() {
        Payment payment = createPayment(createAppointment(LocalDateTime.now().minusDays(1)));
        Assertions.assertNotNull(paymentService.findByAppointmentId(payment.getAppointment().getId()));
    }

    @Test
    void testNotFoundByAppointmentId() {
        Assertions.assertThrows(HCNotFoundException.class, () -> paymentService.findByAppointmentId(1L));
    }

    private void markAppointmentAsFinished(Appointment appointment) {
        appointment.setStatus(AppointmentStatus.FINISHED);
        appointmentService.save(appointment);
    }

    private void assertFields(Payment payment, Double amount, LocalDateTime date, Appointment appointment, PaymentType type) {
        Assertions.assertNotNull(payment.getId(), "Doesn't have id");
        Assertions.assertEquals(amount, payment.getAmount(), "Amount doesn't match");
        Assertions.assertEquals(date, payment.getPaymentDate(), "Date doesn't match");
        Assertions.assertEquals(appointment.getId(), payment.getAppointment().getId(), "Appointment doesn't match");
        Assertions.assertEquals(type, payment.getType(), "Type doesn't match");
    }

    private void assertHasFields(Payment payment) {
        Assertions.assertNotNull(payment.getId(), "Doesn't have id");
        Assertions.assertNotNull(payment.getAmount(), "Doesn't have amount");
        Assertions.assertNotNull(payment.getPaymentDate(), "Doesn't have paymentDate");
        Assertions.assertNotNull(payment.getAppointment(), "Doesn't have appointment");
        Assertions.assertNotNull(payment.getType(), "Doesn't have type");
    }
}
