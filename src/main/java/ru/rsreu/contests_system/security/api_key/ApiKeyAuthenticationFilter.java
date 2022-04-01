package ru.rsreu.contests_system.security.api_key;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@AllArgsConstructor
public class ApiKeyAuthenticationFilter extends GenericFilterBean {
    private final String apiKeyHeaderName;

    private final ApiKeyProvider apiKeyProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!apiKeyProvider.validateApiKey(resolveApiKey((HttpServletRequest)request))) {
            throw new BadCredentialsException("Invalid api key");
        } else {
            chain.doFilter(request, response);
        }
    }

    private String resolveApiKey(HttpServletRequest request) {
        return request.getHeader(apiKeyHeaderName);
    }
}

