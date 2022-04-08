package ru.rsreu.contests_system.api.organization.resource.dto.organization_info;

import ru.rsreu.contests_system.api.organization.event.Event;

import java.util.Set;

public record OrganizationInfoResponse(String id, String name, String description, String pictureUrl,
                                       Set<Event> events) {
}
