package ru.rsreu.contests_system.api.organization_creating_application.resource.dto.check_lead_email;

import org.springframework.stereotype.Component;

@Component
public record CheckLeaderEmailUniqueMapper() {
    public CheckLeaderEmailUniqueResponse toResponse(boolean isUnique) {
        return new CheckLeaderEmailUniqueResponse(isUnique);
    }
}
