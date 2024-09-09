package com.vasiu_catalina.beauty_salon.validation.validDateFormat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidDateFormatValidator.class)
public @interface ValidDateFormat {
    String message() default "Invalid date format provided. Please use yyyy=MM-dd.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
