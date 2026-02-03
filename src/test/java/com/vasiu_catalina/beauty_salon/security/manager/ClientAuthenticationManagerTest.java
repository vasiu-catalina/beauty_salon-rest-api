package com.vasiu_catalina.beauty_salon.security.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.vasiu_catalina.beauty_salon.service.impl.ClientServiceTest;
import com.vasiu_catalina.beauty_salon.entity.Client;
import com.vasiu_catalina.beauty_salon.security.exception.BadCredentialsException;
import com.vasiu_catalina.beauty_salon.service.IClientService;

@ExtendWith(MockitoExtension.class)
class ClientAuthenticationManagerTest {

    @Mock
    private IClientService clientService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private ClientAuthenticationManager authenticationManager;

    @Test
    void authenticateReturnsTokenWhenPasswordMatches() {
        Client client = ClientServiceTest.createClient();
        client.setPassword("hashed");
        when(clientService.getClientByEmail("client@example.com")).thenReturn(client);
        when(passwordEncoder.matches("plain", "hashed")).thenReturn(true);

        Authentication result = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("client@example.com", "plain"));

        assertEquals("client@example.com", result.getName());
        assertEquals("hashed", result.getCredentials());
    }

    @Test
    void authenticateThrowsWhenPasswordDoesNotMatch() {
        Client client = ClientServiceTest.createClient();
        client.setPassword("hashed");
        when(clientService.getClientByEmail("client@example.com")).thenReturn(client);
        when(passwordEncoder.matches("plain", "hashed")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("client@example.com", "plain")));
    }
}
