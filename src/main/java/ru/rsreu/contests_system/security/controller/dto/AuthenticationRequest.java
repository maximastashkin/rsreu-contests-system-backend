package ru.rsreu.contests_system.security.controller.dto;

import lombok.Data;

public record AuthenticationRequest(String username, String password) {
}
