package ru.rsreu.contests_system.security.refresh.dto;

import javax.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank
        String accessToken,
        @NotBlank
        String refreshToken,
        @NotBlank
        String username) {
}
