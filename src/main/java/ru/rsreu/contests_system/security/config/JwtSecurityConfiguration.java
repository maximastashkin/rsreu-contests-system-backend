package ru.rsreu.contests_system.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.rsreu.contests_system.security.jwt.JwtConfigurer;

@EnableWebSecurity
@Order(1)
public class JwtSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final JwtConfigurer jwtConfigurer;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    public JwtSecurityConfiguration(
            JwtConfigurer jwtConfigurer,
            AuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtConfigurer = jwtConfigurer;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize ->
                        authorize
                                .antMatchers("**/users/**").hasAuthority("ADMIN")
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