package com.jcv.hyperclean.validator;

import com.jcv.hyperclean.dto.request.VehicleRequestDTO;
import com.jcv.hyperclean.enums.VehicleType;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

class VehicleValidatorTestCase extends BaseValidatorTest {
    private static final String blankString = "       ";
    private static final String validModel = "Fitito";
    private static final String validOldLicense = "ABC123";
    private static final String validNewLicense = "AB123CD";
    private static final String invalidLicense = "ABC";
    private static final Long validCustomerId = 1L;
    private static final Long invalidCustomerId = 0L;
    private static final VehicleType validType = VehicleType.SUPERCAR;

    @Test
    public void testEmptyRequest() {
        VehicleRequestDTO requestDTO = new VehicleRequestDTO();
        validateRequestDTO(requestDTO, false);
    }

    @ParameterizedTest
    @MethodSource("modelValidationArgs")
    void testRequestModelValidations(Pair<String, Boolean> param) {
        VehicleRequestDTO requestDTO = new VehicleRequestDTO(param.getLeft(), validOldLicense, validCustomerId, validType);
        validateRequestDTO(requestDTO, param.getRight());
    }

    private static List<Pair<String, Boolean>> modelValidationArgs() {
        return List.of(
                Pair.of(null, false),
                Pair.of(blankString, false),
                Pair.of(validModel, true)
        );
    }

    @ParameterizedTest
    @MethodSource("licensePlateValidationArgs")
    void testRequestLicensePlateValidations(Pair<String, Boolean> param) {
        VehicleRequestDTO requestDTO = new VehicleRequestDTO(validModel, param.getLeft(), validCustomerId, validType);
        validateRequestDTO(requestDTO, param.getRight());
    }

    private static List<Pair<String, Boolean>> licensePlateValidationArgs() {
        return List.of(
                Pair.of(null, false),
                Pair.of(blankString, false),
                Pair.of(invalidLicense, false),
                Pair.of(validOldLicense, true),
                Pair.of(validNewLicense, true)
        );
    }

    @ParameterizedTest
    @MethodSource("customerIdValidationArgs")
    void testRequestCustomerIdValidations(Pair<Long, Boolean> param) {
        VehicleRequestDTO requestDTO = new VehicleRequestDTO(validModel, validOldLicense, param.getLeft(), validType);
        validateRequestDTO(requestDTO, param.getRight());
    }

    private static List<Pair<Long, Boolean>> customerIdValidationArgs() {
        return List.of(
                Pair.of(null, false),
                Pair.of(invalidCustomerId, false),
                Pair.of(validCustomerId, true)
        );
    }

    @ParameterizedTest
    @MethodSource("typeValidationArgs")
    void testRequestTypeValidations(Pair<VehicleType, Boolean> param) {
        VehicleRequestDTO requestDTO = new VehicleRequestDTO(validModel, validOldLicense, validCustomerId, param.getLeft());
        validateRequestDTO(requestDTO, param.getRight());
    }

    private static List<Pair<VehicleType, Boolean>> typeValidationArgs() {
        return List.of(
                Pair.of(null, false),
                Pair.of(validType, true)
        );
    }
}
