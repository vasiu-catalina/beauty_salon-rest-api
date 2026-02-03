package com.vasiu_catalina.beauty_salon;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class BeautySalonApplicationTest {

    @Test
    void mainDelegatesToSpringApplicationRun() {
        try (MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {
            mocked.when(() -> SpringApplication.run(eq(BeautySalonApplication.class), any(String[].class)))
                    .thenReturn(null);

            String[] args = new String[] { "--test" };
            BeautySalonApplication.main(args);

            mocked.verify(() -> SpringApplication.run(BeautySalonApplication.class, args));
        }
    }

    @Test
    void bCryptPasswordEncoderBeanIsCreated() {
        BeautySalonApplication app = new BeautySalonApplication();

        BCryptPasswordEncoder encoder = app.bCryptPasswordEncoder();

        assertNotNull(encoder);
    }
}
