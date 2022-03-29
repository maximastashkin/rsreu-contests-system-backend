package ru.rsreu.contests_system.security.auth.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record AuthenticationRequest(
        @Email @NotBlank
        String username,
        @NotBlank
        String password) {
}
