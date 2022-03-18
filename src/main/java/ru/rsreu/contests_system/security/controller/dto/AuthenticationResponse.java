package ru.rsreu.contests_system.security.controller.dto;

import ru.rsreu.contests_system.user.Role;

public record AuthenticationResponse(String token, String refreshToken, Role role) {
}
