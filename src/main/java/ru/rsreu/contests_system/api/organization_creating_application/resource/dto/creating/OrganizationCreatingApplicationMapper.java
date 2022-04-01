package ru.rsreu.contests_system.api.organization_creating_application.resource.dto.creating;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization_creating_application.OrganizationCreatingApplication;

@Component
public record OrganizationCreatingApplicationMapper() {
    public OrganizationCreatingApplication toOrganizationCreatingApplication(
            OrganizationCreatingApplicationRequest organizationCreatingApplicationRequest) {
        return OrganizationCreatingApplication.builder()
                .name(organizationCreatingApplicationRequest.name())
                .organizationEmail(organizationCreatingApplicationRequest.organizationEmail())
                .organizationPhone(organizationCreatingApplicationRequest.organizationPhone())
                .leaderFirstName(organizationCreatingApplicationRequest.leaderFirstName())
                .leaderEmail(organizationCreatingApplicationRequest.leaderLastName())
                .leaderMiddleName(organizationCreatingApplicationRequest.leaderMiddleName())
                .leaderEmail(organizationCreatingApplicationRequest.leaderEmail())
                .build();
    }
}
