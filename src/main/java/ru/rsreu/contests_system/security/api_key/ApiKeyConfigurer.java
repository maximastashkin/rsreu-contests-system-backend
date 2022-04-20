package ru.rsreu.contests_system.security.api_key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.security.jwt.JwtTokenFilter;

@Component
public class ApiKeyConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Value("${security.api_key.header}")
    private String apiKeyHeaderName;

    @Value("${security.api_key.valid}")
    private String validApiKey;

    @Override
    public void configure(HttpSecurity http) {
        http.addFilterBefore(new ApiKeyAuthenticationFilter(
                apiKeyHeaderName,
                new ApiKeyProvider(validApiKey)), JwtTokenFilter.class);
        http.addFilterBefore(new ApiKeyExceptionFilterHandler(), ApiKeyAuthenticationFilter.class);
    }
}
