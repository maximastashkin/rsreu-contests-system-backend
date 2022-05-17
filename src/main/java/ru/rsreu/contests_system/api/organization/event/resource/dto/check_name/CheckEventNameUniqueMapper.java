package ru.rsreu.contests_system.api.organization.event.resource.dto.check_name;

import org.springframework.stereotype.Component;

@Component
public record CheckEventNameUniqueMapper() {
    public CheckEventNameUniqueResponse toResponse(boolean isUnique) {
        return new CheckEventNameUniqueResponse(isUnique);
    }
}
