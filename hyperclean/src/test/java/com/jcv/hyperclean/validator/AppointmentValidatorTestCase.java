package com.jcv.hyperclean.validator;

import com.jcv.hyperclean.dto.request.AppointmentRequestDTO;
import com.jcv.hyperclean.enums.AppointmentStatus;
import com.jcv.hyperclean.enums.ServiceType;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.List;

public class AppointmentValidatorTestCase extends BaseValidatorTest {
    private static final LocalDateTime validDate = LocalDateTime.now().plusDays(1);
    private static final LocalDateTime invalidDate = LocalDateTime.now();
    private static final AppointmentStatus validStatus = AppointmentStatus.PENDING;
    private static final ServiceType validType = ServiceType.COMPLETE;
    private static final Long invalidVehicleId = 0L;
    private static final Long validVehicleId = 1L;

    @Test
    void testEmptyRequest() {
        AppointmentRequestDTO requestDTO = new AppointmentRequestDTO();
        validateRequestDTO(requestDTO, false);
    }

    @ParameterizedTest
    @MethodSource("dateValidationArgs")
    void testRequestDateValidations(Pair<LocalDateTime, Boolean> param) {
        AppointmentRequestDTO requestDTO = new AppointmentRequestDTO(param.getLeft(), null, validType, validVehicleId);
        validateRequestDTO(requestDTO, param.getRight());
    }

    private static List<Pair<LocalDateTime, Boolean>> dateValidationArgs() {
        return List.of(
                Pair.of(null, false),
                Pair.of(invalidDate, false),
                Pair.of(validDate, true)
        );
    }

    @ParameterizedTest
    @MethodSource("statusValidationArgs")
    void testRequestStatusValidations(Pair<AppointmentStatus, Boolean> param) {
        AppointmentRequestDTO requestDTO = new AppointmentRequestDTO(validDate, param.getLeft(), validType, validVehicleId);
        validateRequestDTO(requestDTO, param.getRight());
    }

    private static List<Pair<AppointmentStatus, Boolean>> statusValidationArgs() {
        return List.of(
                Pair.of(null, true), // Will use default value
                Pair.of(validStatus, true)
        );
    }

    @ParameterizedTest
    @MethodSource("serviceTypeValidationArgs")
    void testServiceTypeValidations(Pair<ServiceType, Boolean> param) {
        AppointmentRequestDTO requestDTO = new AppointmentRequestDTO(validDate, validStatus, param.getLeft(), validVehicleId);
        validateRequestDTO(requestDTO, param.getRight());
    }

    private static List<Pair<ServiceType, Boolean>> serviceTypeValidationArgs() {
        return List.of(
                Pair.of(null, false),
                Pair.of(ServiceType.COMPLETE, true)
        );
    }

    @ParameterizedTest
    @MethodSource("vehicleIdValidationArgs")
    void testVehicleIdValidations(Pair<Long, Boolean> param) {
        AppointmentRequestDTO requestDTO = new AppointmentRequestDTO(validDate, validStatus, validType, param.getLeft());
        validateRequestDTO(requestDTO, param.getRight());
    }

    private static List<Pair<Long, Boolean>> vehicleIdValidationArgs() {
        return List.of(
                Pair.of(null, false),
                Pair.of(invalidVehicleId, false),
                Pair.of(validVehicleId, true)
        );
    }
}
