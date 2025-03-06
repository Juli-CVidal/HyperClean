package com.jcv.hyperclean.validator;

import com.jcv.hyperclean.enums.DateValidationType;
import com.jcv.hyperclean.exception.HCInvalidDateTimeFormat;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

import static com.jcv.hyperclean.util.DateUtils.stringToLocalDateTime;

public class DateValidator implements ConstraintValidator<DateValidation, String> {
    private DateValidationType type;

    @Override
    public void initialize(DateValidation constraintAnnotation) {
        this.type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fieldDate;
        try {
            fieldDate = stringToLocalDateTime(value);
        } catch (IllegalArgumentException | HCInvalidDateTimeFormat e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid date format. Expected format: dd-MM-yyyy HH:mm:ss")
                    .addConstraintViolation();
            return false;
        }

        if (fieldDate.isEqual(now)) {
            return true;
        }

        boolean isValid = switch (type) {
            case PAST -> fieldDate.isBefore(now);
            case FUTURE -> fieldDate.isAfter(now);
        };

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            String errorMessage = String.format("The date must be in the %s or present", type.equals(DateValidationType.PAST) ? "past" : "future");
            context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
        }

        return isValid;
    }
}