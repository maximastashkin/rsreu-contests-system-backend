package ru.rsreu.contests_system.api.organization_creating_application.resource.dto.application_info;

public record OrganizationCreatingApplicationsInfoResponse(
        String id,
        String name,
        String organizationEmail,
        String organizationPhone,
        String leaderFirstName,
        String leaderLastName,
        String leaderMiddleName,
        String leaderEmail
) {
}
