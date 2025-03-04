package com.jcv.hyperclean.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest
public abstract class BaseValidatorTest {
    private Validator validator;

    @BeforeEach
    public void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    protected <T> Set<ConstraintViolation<T>> validate(T requestDTO) {
        return validator.validate(requestDTO);
    }

    protected <T> void validateRequestDTO(T requestDTO, boolean shouldBeValid) {
        Set<ConstraintViolation<T>> constraintViolations = validate(requestDTO);
        Assertions.assertEquals(shouldBeValid, constraintViolations.isEmpty(), constraintViolations.toString());
    }
}
