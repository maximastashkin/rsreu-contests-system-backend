package ru.rsreu.contests_system.api.organization.event.resource.dto.event_info;

import java.time.LocalDateTime;

public record EventInfoResponse(String name, String description,
                                LocalDateTime startDateTime, LocalDateTime endDateTime) {
}
