package ru.rsreu.contests_system.api.organization_creating_application.resource.dto.check_org_email;

import org.springframework.stereotype.Component;

@Component
public record CheckOrganizationEmailUniqueMapper() {
    public CheckOrganizationEmailUniqueResponse toResponse(boolean unique) {
        return new CheckOrganizationEmailUniqueResponse(unique);
    }
}
