package com.vasiu_catalina.beauty_salon.security.manager;

import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.vasiu_catalina.beauty_salon.entity.Client;
import com.vasiu_catalina.beauty_salon.security.exception.BadCredentialsException;
import com.vasiu_catalina.beauty_salon.service.IClientService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
@Primary
public class ClientAuthenticationManager implements AuthenticationManager {
    
    private IClientService clientService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
       
        Client client = clientService.getClientByEmail(authentication.getName());

        if (!bCryptPasswordEncoder.matches(authentication.getCredentials().toString(), client.getPassword())) {
            throw new BadCredentialsException("Incorrect password provided!");
        }

        return new UsernamePasswordAuthenticationToken(authentication.getName(), client.getPassword());
    }
    

}
