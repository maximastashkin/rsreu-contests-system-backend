package ru.rsreu.contests_system.api.organization.resource.dto.organization.organization_info;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.Organization;
import ru.rsreu.contests_system.api.organization.event.resource.dto.event_info.EventInfoMapper;
import ru.rsreu.contests_system.api.user.User;

import java.util.Optional;

@Component
public record OrganizationInfoMapper(
        EventInfoMapper eventInfoMapper) {
    public OrganizationInfoResponse toResponse(Organization organization, Optional<User> candidateForEventChecking) {
        return new OrganizationInfoResponse(
                organization.getId().toString(),
                organization.getName(),
                organization.getDescription(),
                organization.getPictureUrl(),
                organization.getEvents()
                        .stream().map(event ->
                                eventInfoMapper.toResponse(event, candidateForEventChecking)).toList()
        );
    }
}
