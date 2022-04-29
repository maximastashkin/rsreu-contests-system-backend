package ru.rsreu.contests_system.api.organization.event.resource.dto.started_event_info;

import ru.rsreu.contests_system.api.organization.event.EventType;

import java.time.LocalDateTime;
import java.util.List;

public record StartedEventInfoResponse(
        String eventName,
        EventType type,
        String description,
        LocalDateTime maxEndDateTime,
        List<String> taskSolutionsId) {
}
