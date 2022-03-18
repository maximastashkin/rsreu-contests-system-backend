package ru.rsreu.contests_system.security.controller.dto;

public record AuthenticationResponse(String token, String refreshToken) {
}
