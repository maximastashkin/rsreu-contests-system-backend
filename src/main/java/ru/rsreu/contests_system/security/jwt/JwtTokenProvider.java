package ru.rsreu.contests_system.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.security.user.UserDetailsImpl;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private static final String AUTHORITIES_KEY = "roles";

    //@Value("${security.jwt.token.secret-key:secret}")
    private String secretKeyStringRepresentation = "my-32-character-ultra-secure-and-ultra-long-secret";

    @Value("${security.jwt.token.expire-length:3600000}")
    private long tokenExpiringMilliSeconds = 60 * 60 * 1000;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        String secret = Base64.getEncoder().encodeToString(secretKeyStringRepresentation.getBytes());
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createTokenFromAuthentication(Authentication authentication) {
        Date now = new Date();
        return Jwts.builder()
                .setClaims(getClaims(authentication))
                .setIssuedAt(now)
                .setExpiration(getValidityDate(now))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private Date getValidityDate(Date now) {
        return new Date(now.getTime() + tokenExpiringMilliSeconds);
    }

    private Claims getClaims(Authentication authentication) {
        String username = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Claims claims = Jwts.claims().setSubject(username);

        if (!authorities.isEmpty()) {
            claims.put(AUTHORITIES_KEY, authorities.stream().map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(",")));
        }
        return claims;
    }

    public Authentication getAuthenticationFromJwtToken(String jwtToken) {
        Claims claims = getClaimsJws(jwtToken).getBody();
        Object authoritiesClaim = claims.get(AUTHORITIES_KEY);
        Collection<? extends GrantedAuthority> authorities = getAuthorities(authoritiesClaim);

        return new UsernamePasswordAuthenticationToken(
                new UserDetailsImpl(claims.getSubject(), "", authorities),
                jwtToken,
                authorities);
    }

    private Jws<Claims> getClaimsJws(String jwtToken) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwtToken);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Object authoritiesClaim) {
        Collection<? extends GrantedAuthority> authorities;
        if (authoritiesClaim == null) {
            authorities = AuthorityUtils.NO_AUTHORITIES;
        } else {
            authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim.toString());
        }
        return authorities;
    }

    public boolean validateToken(String jwtToken) throws JwtException {
        getClaimsJws(jwtToken);
        return true;
    }
}
