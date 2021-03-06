package ru.rsreu.contests_system.security.jwt;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {
    private static final String HEADER_PREFIX = "Bearer ";
    private static final int START_TOKEN_INDEX = 7;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(
            ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwtToken = resolveToken((HttpServletRequest) request);
        setAuthentication(jwtToken);
        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_PREFIX)) {
            return bearerToken.substring(START_TOKEN_INDEX);
        }
        return null;
    }

    private void setAuthentication(String jwtToken) {
        if (jwtToken != null && jwtTokenProvider.validateToken(jwtToken)) {
            Authentication authentication = jwtTokenProvider.getAuthenticationFromJwtToken(jwtToken);
            addAuthenticationToSecurityContext(authentication);
        }
    }

    private void addAuthenticationToSecurityContext(Authentication authentication) {
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
}
