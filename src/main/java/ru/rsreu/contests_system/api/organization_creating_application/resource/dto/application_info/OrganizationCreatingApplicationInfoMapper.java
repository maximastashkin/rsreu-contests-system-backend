package ru.rsreu.contests_system.api.organization_creating_application.resource.dto.application_info;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization_creating_application.OrganizationCreatingApplication;

@Component
public class OrganizationCreatingApplicationInfoMapper {
    public OrganizationCreatingApplicationsInfoResponse toResponse(
            OrganizationCreatingApplication organizationCreatingApplication) {
        return new OrganizationCreatingApplicationsInfoResponse(
                organizationCreatingApplication.getId().toString(),
                organizationCreatingApplication.getName(),
                organizationCreatingApplication.getOrganizationEmail(),
                organizationCreatingApplication.getOrganizationPhone(),
                organizationCreatingApplication.getLeaderFirstName(),
                organizationCreatingApplication.getLeaderLastName(),
                organizationCreatingApplication.getLeaderMiddleName(),
                organizationCreatingApplication.getLeaderEmail()
        );
    }
}
