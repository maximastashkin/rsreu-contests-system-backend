package ru.rsreu.contests_system.api.organization.event.resource.dto.event_types;

import ru.rsreu.contests_system.api.organization.event.EventType;

public record EventTypesResponse(
        EventType type,
        String stringRepresentation) {
}
