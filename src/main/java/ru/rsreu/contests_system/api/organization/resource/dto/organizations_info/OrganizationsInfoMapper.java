package ru.rsreu.contests_system.api.organization.resource.dto.organizations_info;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.Organization;

@Component
public class OrganizationsInfoMapper {
    public OrganizationsInfoResponse toResponse(Organization organization) {
        return new OrganizationsInfoResponse(
                organization.getId().toString(),
                organization.getName(),
                organization.getDescription(),
                organization.getPictureUrl()
        );
    }
}
