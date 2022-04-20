package ru.rsreu.contests_system.api.organization.resource.dto.event.event_info;

import ru.rsreu.contests_system.api.organization.event.EventType;

import java.time.LocalDateTime;

public record EventInfoResponse(String id, String name, EventType eventType, String description,
                                LocalDateTime startDateTime,
                                LocalDateTime endDateTime, boolean isFollowed) {
}
