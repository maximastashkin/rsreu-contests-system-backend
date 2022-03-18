package ru.rsreu.contests_system.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.rsreu.contests_system.security.controller.dto.AuthenticationRequest;
import ru.rsreu.contests_system.security.controller.dto.AuthenticationResponse;
import ru.rsreu.contests_system.security.jwt.JwtTokenProvider;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthenticationController(
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest authenticationRequest) {
        Authentication authentication = getAuthenticationFromRequest(authenticationRequest);
        String token = jwtTokenProvider.createTokenFromAuthentication(authentication);
        return new ResponseEntity<>(
                new AuthenticationResponse(token, null),
                HttpStatus.OK);
    }

    private Authentication getAuthenticationFromRequest(AuthenticationRequest authenticationRequest) {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            authenticationRequest.username(),
                            authenticationRequest.password()
                    )
            );
        } catch (UsernameNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
