package ru.rsreu.contests_system.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import ru.rsreu.contests_system.security.api_key.ApiKeyConfigurer;

@EnableWebSecurity
@Order(2)
public class ApiKeySecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final String[] AUTH_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html/**"
    };

    private final ApiKeyConfigurer apiKeyConfigurer;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    public ApiKeySecurityConfiguration(
            ApiKeyConfigurer apiKeyConfigurer,
            AuthenticationEntryPoint authenticationEntryPoint) {
        this.apiKeyConfigurer = apiKeyConfigurer;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(authorize ->
                        authorize.antMatchers(AUTH_WHITELIST).permitAll()
                                .antMatchers("/api/**").authenticated())
                .apply(apiKeyConfigurer)
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
    }
}
