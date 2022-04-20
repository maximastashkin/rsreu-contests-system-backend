package ru.rsreu.contests_system.api.organization.resource.dto.organization.organization_info;

import ru.rsreu.contests_system.api.organization.event.resource.dto.event_info.EventInfoResponse;

import java.util.List;

public record OrganizationInfoResponse(String id, String name, String description, String pictureUrl,
                                       List<EventInfoResponse> events) {
}
