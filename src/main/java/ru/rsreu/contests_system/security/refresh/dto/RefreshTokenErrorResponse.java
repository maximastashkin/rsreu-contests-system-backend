package ru.rsreu.contests_system.security.refresh.dto;

import ru.rsreu.contests_system.security.refresh.exception.RefreshTokenErrorStatus;

public record RefreshTokenErrorResponse(RefreshTokenErrorStatus refreshError) {
}
