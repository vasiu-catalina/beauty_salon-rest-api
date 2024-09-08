package com.vasiu_catalina.beauty_salon.validation;

import com.vasiu_catalina.beauty_salon.validation.validators.MinAgeValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinAgeValidator.class)
public @interface MinAge {
    String message() default "Minimum age is 14.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

