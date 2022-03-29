package ru.rsreu.contests_system.api.user.resource.dto.check_mail;

import org.springframework.stereotype.Component;

@Component
public record CheckMailMapper() {
    public CheckMailResponse toResponse(boolean unique) {
        return new CheckMailResponse(unique);
    }
}
