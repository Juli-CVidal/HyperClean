package com.jcv.hyperclean.validator;

import com.jcv.hyperclean.dto.request.PaymentRequestDTO;
import com.jcv.hyperclean.enums.PaymentType;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.List;

class PaymentValidatorTestCase extends BaseValidatorTest {

    private static final Double validAmount = 100.0;
    private static final Double invalidAmount = -10.0;
    private static final LocalDateTime validPaymentDate = LocalDateTime.now();
    private static final LocalDateTime invalidPaymentDate = LocalDateTime.now().plusDays(1);
    private static final PaymentType validType = PaymentType.CASH;
    private static final Long validAppointmentId = 1L;
    private static final Long invalidAppointmentId = 0L;

    @Test
    void testEmptyRequest() {
        PaymentRequestDTO requestDTO = new PaymentRequestDTO();
        validateRequestDTO(requestDTO, false);
    }

    @ParameterizedTest
    @MethodSource("amountValidationArgs")
    void testRequestAmountValidations(Pair<Double, Boolean> param) {
        PaymentRequestDTO requestDTO = new PaymentRequestDTO(param.getLeft(), validPaymentDate, validType, validAppointmentId);
        validateRequestDTO(requestDTO, param.getRight());
    }

    private static List<Pair<Double, Boolean>> amountValidationArgs() {
        return List.of(
                Pair.of(null, false),
                Pair.of(invalidAmount, false),
                Pair.of(validAmount, true)
        );
    }

    @ParameterizedTest
    @MethodSource("paymentDateValidationArgs")
    void testRequestPaymentDateValidations(Pair<LocalDateTime, Boolean> param) {
        PaymentRequestDTO requestDTO = new PaymentRequestDTO(validAmount, param.getLeft(), validType, validAppointmentId);
        validateRequestDTO(requestDTO, param.getRight());
    }

    private static List<Pair<LocalDateTime, Boolean>> paymentDateValidationArgs() {
        return List.of(
                Pair.of(null, true), // Will use default value
                Pair.of(invalidPaymentDate, false),
                Pair.of(validPaymentDate, true)
        );
    }

    @ParameterizedTest
    @MethodSource("typeValidationArgs")
    void testRequestTypeValidations(Pair<PaymentType, Boolean> param) {
        PaymentRequestDTO requestDTO = new PaymentRequestDTO(validAmount, validPaymentDate, param.getLeft(), validAppointmentId);
        validateRequestDTO(requestDTO, param.getRight());
    }

    private static List<Pair<PaymentType, Boolean>> typeValidationArgs() {
        return List.of(
                Pair.of(null, true), // Will use default value
                Pair.of(validType, true)
        );
    }

    @ParameterizedTest
    @MethodSource("appointmentIdValidationArgs")
    void testRequestAppointmentIdValidations(Pair<Long, Boolean> param) {
        PaymentRequestDTO requestDTO = new PaymentRequestDTO(validAmount, validPaymentDate, validType, param.getLeft());
        validateRequestDTO(requestDTO, param.getRight());
    }

    private static List<Pair<Long, Boolean>> appointmentIdValidationArgs() {
        return List.of(
                Pair.of(null, false),
                Pair.of(invalidAppointmentId, false),
                Pair.of(validAppointmentId, true)
        );
    }

}