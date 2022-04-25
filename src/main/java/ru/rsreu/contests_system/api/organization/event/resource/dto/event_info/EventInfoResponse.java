package ru.rsreu.contests_system.api.organization.event.resource.dto.event_info;

import ru.rsreu.contests_system.api.organization.event.EventType;

import java.time.LocalDateTime;

public record EventInfoResponse(
        String id,
        String name,
        EventType type,
        String description,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        ParticipantStatus participantStatus) {
}
