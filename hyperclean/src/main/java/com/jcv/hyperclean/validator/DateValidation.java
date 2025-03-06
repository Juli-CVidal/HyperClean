package com.jcv.hyperclean.validator;


import com.jcv.hyperclean.enums.DateValidationType;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateValidation {
    String message() default "You need to enter a valid future date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    DateValidationType type();
}