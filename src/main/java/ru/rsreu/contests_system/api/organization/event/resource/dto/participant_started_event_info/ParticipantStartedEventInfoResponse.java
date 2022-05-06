package ru.rsreu.contests_system.api.organization.event.resource.dto.participant_started_event_info;

import ru.rsreu.contests_system.api.organization.event.EventType;

import java.time.LocalDateTime;
import java.util.List;

public record ParticipantStartedEventInfoResponse(
        String eventName,
        EventType type,
        String description,
        LocalDateTime maxEndDateTime,
        List<String> tasksSolutionsIds) {
}
