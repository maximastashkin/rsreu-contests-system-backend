package ru.rsreu.contests_system.security.refresh;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rsreu.contests_system.security.auth.dto.AuthenticationResponse;
import ru.rsreu.contests_system.security.refresh.dto.RefreshTokenErrorResponse;
import ru.rsreu.contests_system.security.refresh.dto.RefreshTokenRequest;
import ru.rsreu.contests_system.security.refresh.exception.BlackListTokenException;
import ru.rsreu.contests_system.security.refresh.exception.CompromisedTokenException;
import ru.rsreu.contests_system.security.refresh.exception.InvalidTokenException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/refresh")
@AllArgsConstructor
public class RefreshController {
    private final RefreshService refreshService;

    @Operation(summary = "${api.refresh.operation}")
    @PostMapping(consumes = "application/json", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.refresh.response-codes.ok}"),
            @ApiResponse(responseCode = "400", description = "${api.refresh.response-codes.bad-request}",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "${api.refresh.response-codes.unauth}",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RefreshTokenErrorResponse.class)
                            )
                    }
            )
    })
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
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
}