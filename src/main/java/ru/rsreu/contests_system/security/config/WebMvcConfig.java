package ru.rsreu.contests_system.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${security.frontend_app.url_pattern}")
    private String frontendAppUrlPattern;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(frontendAppUrlPattern)
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "OPTIONS");
    }
}
