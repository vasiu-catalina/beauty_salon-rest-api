package com.vasiu_catalina.beauty_salon.validation.validDateFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidDateFormatValidator implements ConstraintValidator<ValidDateFormat, LocalDate> {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
         try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            birthDate.format(formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
