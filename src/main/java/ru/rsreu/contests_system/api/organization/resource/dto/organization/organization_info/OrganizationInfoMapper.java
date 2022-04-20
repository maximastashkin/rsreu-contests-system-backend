package ru.rsreu.contests_system.api.organization.resource.dto.organization.organization_info;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.Organization;
import ru.rsreu.contests_system.api.organization.event.resource.dto.event_info.EventInfoMapper;

import java.util.stream.Collectors;

@Component
public class OrganizationInfoMapper {
    public OrganizationInfoResponse toResponse(Organization organization) {
        return new OrganizationInfoResponse(
                organization.getId().toString(),
                organization.getName(),
                organization.getDescription(),
                organization.getPictureUrl(),
                organization.getEvents()
                        .stream().map(new EventInfoMapper()::toResponse).collect(Collectors.toList())
        );
    }

}
