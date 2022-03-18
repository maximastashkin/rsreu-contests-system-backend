package ru.rsreu.contests_system.security.refresh.dto;

public record RefreshTokenRequest(String accessToken, String refreshToken, String username) {
}
