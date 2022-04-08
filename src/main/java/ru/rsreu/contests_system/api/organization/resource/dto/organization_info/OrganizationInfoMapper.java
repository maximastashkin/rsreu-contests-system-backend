package ru.rsreu.contests_system.api.organization.resource.dto.organization_info;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.Organization;

@Component
public class OrganizationInfoMapper {
    public OrganizationInfoResponse toResponse(Organization organization) {
        return new OrganizationInfoResponse(
                organization.getId().toString(),
                organization.getName(),
                organization.getDescription(),
                organization.getPictureUrl(),
                organization.getEvents()
        );
    }

}
