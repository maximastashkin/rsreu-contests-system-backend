package ru.rsreu.contests_system.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.rsreu.contests_system.security.controller.dto.AuthenticationRequest;
import ru.rsreu.contests_system.security.controller.dto.AuthenticationResponse;
import ru.rsreu.contests_system.security.jwt.JwtTokenProvider;
import ru.rsreu.contests_system.user.service.UserService;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    @Autowired
    public AuthenticationController(
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest authenticationRequest) {
        try {
            String username = authenticationRequest.username();
            String password = authenticationRequest.password();
            var authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String token = jwtTokenProvider.createTokenFromAuthentication(authentication);
            return new ResponseEntity<>(
                    new AuthenticationResponse(token, null, userService.getUserByEmail(username).getRole()),
                    HttpStatus.OK);
        } catch (AuthenticationException exception) {
            throw new BadCredentialsException("error");
        }
    }
}
