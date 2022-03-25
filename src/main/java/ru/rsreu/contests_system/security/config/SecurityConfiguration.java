package ru.rsreu.contests_system.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.rsreu.contests_system.security.jwt.JwtConfigurer;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final String[] AUTH_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html/**",
            "/**/signup",
            "/**/auth",
            "/**/check-mail",
            "/**/refresh"
    };
    private final JwtConfigurer jwtConfigurer;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    public SecurityConfiguration(
            JwtConfigurer jwtConfigurer,
            AuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtConfigurer = jwtConfigurer;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(authorize ->
                        authorize
                                .antMatchers(AUTH_WHITELIST).permitAll()
                                .antMatchers("/api/users/**").hasAuthority("ADMIN")
                                .anyRequest().denyAll()
                )
                .apply(jwtConfigurer)
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}