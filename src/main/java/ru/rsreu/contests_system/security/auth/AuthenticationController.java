package ru.rsreu.contests_system.security.auth;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import ru.rsreu.contests_system.security.auth.dto.AuthenticationRequest;
import ru.rsreu.contests_system.security.auth.dto.AuthenticationResponse;
import ru.rsreu.contests_system.security.jwt.JwtTokenProvider;
import ru.rsreu.contests_system.security.refresh.RefreshTokenProvider;
import ru.rsreu.contests_system.user.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final RefreshTokenProvider refreshTokenProvider;

    private final UserService userService;

    @Autowired
    public AuthenticationController(
            @Qualifier("jwtAuthenticationManager") AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider, RefreshTokenProvider refreshTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenProvider = refreshTokenProvider;
        this.userService = userService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.auth.response-codes.ok.desc}"),
            @ApiResponse(responseCode = "401", description = "${api.auth.response-codes.unauth.desc}",
            content = {
               @Content()
            }),
            @ApiResponse(responseCode = "404", description = "${api.auth.response-codes.not-found.desc}",
                    content = {
                            @Content()
                    })
    })
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest authenticationRequest) {
        Authentication authentication = getAuthenticationFromRequest(authenticationRequest);
        String token = jwtTokenProvider.createTokenFromAuthentication(authentication);
        String refreshToken =  refreshTokenProvider.createTokenFromJwtToken(token);
        userService.addRefreshToken(authentication.getName(), refreshToken);
        return new ResponseEntity<>(
                new AuthenticationResponse(token,
                       refreshToken),
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