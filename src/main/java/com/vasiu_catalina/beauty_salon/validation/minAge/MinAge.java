package com.vasiu_catalina.beauty_salon.validation.minAge;

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

    int value();

    String message() default "Minimum age is {value}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

