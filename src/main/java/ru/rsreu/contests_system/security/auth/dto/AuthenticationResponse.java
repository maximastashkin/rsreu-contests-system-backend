package ru.rsreu.contests_system.security.auth.dto;

public record AuthenticationResponse(String token, String refreshToken) {
}
