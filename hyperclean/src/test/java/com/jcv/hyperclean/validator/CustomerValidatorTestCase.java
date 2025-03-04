package com.jcv.hyperclean.validator;

import com.jcv.hyperclean.dto.request.CustomerRequestDTO;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

class CustomerValidatorTestCase extends BaseValidatorTest {
    private static final String blankString = "       ";
    private static final String validName = "John Doe";
    private static final String validEmail = "john.doe@example.com";
    private static final String invalidEmail = "john.doeexample.com";
    private static final String validPhone = "2612222222";
    private static final String invalidPhone = "261";

    @Test
    void testEmptyRequest() {
        CustomerRequestDTO requestDTO = new CustomerRequestDTO();
        validateRequestDTO(requestDTO, false);
    }

    @ParameterizedTest
    @MethodSource("nameValidationArgs")
    void testRequestNameValidations(Pair<String, Boolean> param) {
        CustomerRequestDTO requestDTO = new CustomerRequestDTO(param.getLeft(), validEmail, validPhone);
        validateRequestDTO(requestDTO, param.getRight());
    }

    private static List<Pair<String, Boolean>> nameValidationArgs() {
        return List.of(
                Pair.of(null, false),
                Pair.of(blankString, false),
                Pair.of(validName, true)
        );
    }

    @ParameterizedTest
    @MethodSource("emailValidationArgs")
    void testRequestEmailValidations(Pair<String, Boolean> param) {
        CustomerRequestDTO requestDTO = new CustomerRequestDTO(validName, param.getLeft(), validPhone);
        validateRequestDTO(requestDTO, param.getRight());
    }

    private static List<Pair<String, Boolean>> emailValidationArgs() {
        return List.of(
                Pair.of(null, false),
                Pair.of(blankString, false),
                Pair.of(invalidEmail, false),
                Pair.of(validEmail, true)
        );
    }

    @ParameterizedTest
    @MethodSource("phoneValidationArgs")
    void testRequestPhoneValidations(Pair<String, Boolean> param) {
        CustomerRequestDTO requestDTO = new CustomerRequestDTO(validName, validEmail, param.getLeft());
        validateRequestDTO(requestDTO, param.getRight());
    }

    private static List<Pair<String, Boolean>> phoneValidationArgs() {
        return List.of(
                Pair.of(null, false),
                Pair.of(blankString, false),
                Pair.of(invalidPhone, false),
                Pair.of(validPhone, true)
        );
    }
}
