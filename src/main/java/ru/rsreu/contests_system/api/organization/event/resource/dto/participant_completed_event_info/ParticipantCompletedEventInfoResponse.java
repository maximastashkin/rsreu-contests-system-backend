package ru.rsreu.contests_system.api.organization.event.resource.dto.participant_completed_event_info;

import ru.rsreu.contests_system.api.organization.event.resource.dto.participant_started_event_info.ParticipantStartedEventInfoResponse;

public record ParticipantCompletedEventInfoResponse(
        ParticipantStartedEventInfoResponse participantEventInfo,
        int score,
        int maxScore
) {
}
