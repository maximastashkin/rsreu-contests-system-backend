package ru.rsreu.contests_system.security.api_key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component(value = "apiKeyAuthenticationManager")
public class ApiKeyAuthenticationManager implements AuthenticationManager {
    @Value("${security.api.key}")
    private String validApiKey;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        boolean authenticated = authentication.getPrincipal().equals(validApiKey);
        authentication.setAuthenticated(authenticated);
        if (!authenticated) {
            throw new BadCredentialsException("Bad api key");
        }
        return authentication;
    }
}
