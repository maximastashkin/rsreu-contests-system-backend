package ru.rsreu.contests_system.security.refresh;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rsreu.contests_system.security.auth.dto.AuthenticationResponse;
import ru.rsreu.contests_system.security.refresh.dto.RefreshTokenErrorRequest;
import ru.rsreu.contests_system.security.refresh.dto.RefreshTokenRequest;
import ru.rsreu.contests_system.security.refresh.exception.BlackListTokenException;
import ru.rsreu.contests_system.security.refresh.exception.CompromisedTokenException;
import ru.rsreu.contests_system.security.refresh.exception.InvalidTokenException;
import ru.rsreu.contests_system.security.refresh.exception.RefreshTokenErrorStatus;

@RestController
@RequestMapping("/api/refresh")
public class RefreshController {
    private final RefreshService refreshService;

    @Autowired
    public RefreshController(RefreshService refreshService) {
        this.refreshService = refreshService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.refresh.response-codes.ok.desc}"),
            @ApiResponse(responseCode = "401", description = "${api.refresh.response-codes.unauth.desc}",
            content = {
                   @Content(
                           mediaType = "application/json",
                           schema = @Schema(implementation = RefreshTokenErrorRequest.class)
                   )
            })
    })
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.refreshToken();
        String accessToken = refreshTokenRequest.accessToken();
        String username = refreshTokenRequest.username();
        if (refreshService.isValidRefreshing(username, accessToken, refreshToken)) {
            String newAccessToken = refreshService.generateNewAccessToken(username);
            String newRefreshToken = refreshService.generateNewRefreshToken(newAccessToken);
            if (refreshService.deleteUserRefreshToken(username, refreshToken) != 0) {
                refreshService.addUserRefreshToken(username, newRefreshToken);
                return new ResponseEntity<>(
                        new AuthenticationResponse(newAccessToken, newRefreshToken), HttpStatus.OK);
            }
        } else if (!refreshService.isRefreshTokenStructure(refreshToken, accessToken)) {
            throw new InvalidTokenException();
        }
        if (refreshService.isCompromisedRefreshToken(refreshToken)) {
            throw new CompromisedTokenException();
        } else {
            throw new BlackListTokenException();
        }
    }

    @ExceptionHandler(value = {InvalidTokenException.class})
    public ResponseEntity<RefreshTokenErrorRequest> handleInvalidTokenException() {
        return formRefreshTokenErrorRequestResponseEntity(RefreshTokenErrorStatus.INVALID_TOKEN);
    }

    private ResponseEntity<RefreshTokenErrorRequest> formRefreshTokenErrorRequestResponseEntity(
            RefreshTokenErrorStatus status) {
        return new ResponseEntity<>(
                new RefreshTokenErrorRequest(status), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {CompromisedTokenException.class})
    public ResponseEntity<RefreshTokenErrorRequest> handleCompromisedTokenException() {
        return formRefreshTokenErrorRequestResponseEntity(RefreshTokenErrorStatus.COMPROMISED);
    }

    @ExceptionHandler(value = {BlackListTokenException.class})
    public ResponseEntity<RefreshTokenErrorRequest> handleBlackListTokenException() {
        return formRefreshTokenErrorRequestResponseEntity(RefreshTokenErrorStatus.BLACK_LIST);
    }
}