package ru.rsreu.contests_system.security.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rsreu.contests_system.security.auth.dto.AuthenticationRequest;
import ru.rsreu.contests_system.security.auth.dto.AuthenticationResponse;
import ru.rsreu.contests_system.security.jwt.JwtTokenProvider;
import ru.rsreu.contests_system.security.refresh.RefreshTokenProvider;
import ru.rsreu.contests_system.api.user.service.UserService;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;
    private final UserService userService;

    @Operation(summary = "${api.auth.operation}")
    @PostMapping(consumes = "application/json", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.auth.response-codes.ok}"),
            @ApiResponse(responseCode = "400", description = "${api.auth.response-codes.bad-request}",
                    content = {@Content()}),
            @ApiResponse(responseCode = "401", description = "${api.auth.response-codes.unauth}",
                    content = {@Content()}),
            @ApiResponse(responseCode = "404", description = "${api.auth.response-codes.not-found}",
                    content = {@Content()})
    })
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest authenticationRequest) {
        Authentication authentication = getAuthenticationFromRequest(authenticationRequest);
        String token = jwtTokenProvider.createTokenFromAuthentication(authentication);
        String refreshToken = refreshTokenProvider.createTokenFromJwtToken(token);
        userService.addRefreshToken(authentication.getName(), refreshToken);
        return new ResponseEntity<>(
                new AuthenticationResponse(token,
                        refreshToken),
                HttpStatus.OK);
    }

    private Authentication getAuthenticationFromRequest(AuthenticationRequest authenticationRequest) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        authenticationRequest.username(),
                        authenticationRequest.password()
                )
        );
    }
}
