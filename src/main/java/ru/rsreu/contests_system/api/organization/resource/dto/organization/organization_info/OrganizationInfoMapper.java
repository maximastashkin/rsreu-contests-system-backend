package ru.rsreu.contests_system.api.organization.resource.dto.organization.organization_info;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.Organization;
import ru.rsreu.contests_system.api.organization.event.resource.dto.event_info.EventInfoMapper;

@Component
@AllArgsConstructor
public class OrganizationInfoMapper {
    private final EventInfoMapper eventInfoMapper;

    public OrganizationInfoResponse toResponse(Organization organization, Authentication authentication) {
        return new OrganizationInfoResponse(
                organization.getId().toString(),
                organization.getName(),
                organization.getDescription(),
                organization.getPictureUrl(),
                organization.getEvents()
                        .stream().map(event ->
                                eventInfoMapper.toResponse(event, authentication)).toList()
        );
    }
}
