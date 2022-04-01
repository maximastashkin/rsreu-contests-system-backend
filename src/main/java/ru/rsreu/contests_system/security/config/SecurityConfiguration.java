package ru.rsreu.contests_system.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.rsreu.contests_system.security.api_key.ApiKeyConfigurer;
import ru.rsreu.contests_system.security.jwt.JwtConfigurer;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final String[] AUTH_WHITELIST = {
            "/**/signup",
            "/**/auth",
            "/**/check-mail",
            "/**/refresh",
            "/**/check-organization-phone",
            "/**/check-organization-email",
            "/**/check-leader-email",
            "/**/applications/"
    };

    private static final String[] API_KEY_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html/**",
    };

    private final JwtConfigurer jwtConfigurer;

    private final ApiKeyConfigurer apiKeyConfigurer;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    public SecurityConfiguration(JwtConfigurer jwtConfigurer, ApiKeyConfigurer apiKeyConfigurer, AuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtConfigurer = jwtConfigurer;
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
                                .antMatchers("/api/users/**").hasAuthority("ADMIN")
                                .anyRequest().denyAll()
                )
                .apply(jwtConfigurer)
                .and()
                .apply(apiKeyConfigurer)
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(API_KEY_WHITELIST);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}