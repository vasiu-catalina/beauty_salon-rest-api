package com.vasiu_catalina.beauty_salon.validation.validators;

import java.time.LocalDate;
import java.time.Period;

import com.vasiu_catalina.beauty_salon.validation.MinAge;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MinAgeValidator implements ConstraintValidator<MinAge, LocalDate> {

    private static final int MIN_AGE = 14;

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        if (birthDate == null) {
            return false;
        }
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        return MIN_AGE <= age;
    }
}
