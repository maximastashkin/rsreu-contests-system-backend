package ru.rsreu.contests_system.api.organization_creating_application.resource.dto.check_phone;

import org.springframework.stereotype.Component;

@Component
public class CheckOrganizationPhoneUniqueMapper {
    public CheckOrganizationPhoneUniqueResponse toResponse(boolean isUnique) {
        return new CheckOrganizationPhoneUniqueResponse(isUnique);
    }
}
