package com.vasiu_catalina.beauty_salon.validation.minAge;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class MinAgeValidatorTest {

    @Test
    void validatesMinimumAge() {
        MinAgeValidator validator = new MinAgeValidator();
        MinAge annotation = mock(MinAge.class);
        when(annotation.value()).thenReturn(18);
        validator.initialize(annotation);

        assertTrue(validator.isValid(LocalDate.now().minusYears(20), null));
        assertTrue(validator.isValid(LocalDate.now().minusYears(18), null));
        assertFalse(validator.isValid(LocalDate.now().minusYears(17), null));
    }

    @Test
    void nullBirthDateIsInvalid() {
        MinAgeValidator validator = new MinAgeValidator();
        MinAge annotation = mock(MinAge.class);
        when(annotation.value()).thenReturn(18);
        validator.initialize(annotation);

        assertFalse(validator.isValid(null, null));
    }
}
