package ru.rsreu.contests_system.api.organization.event.resource.dto.participant_completed_event_info;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.organization.event.participant_info.ParticipantInfo;
import ru.rsreu.contests_system.api.organization.event.resource.dto.participant_started_event_info.ParticipantStartedEventInfoMapper;

@Component
public record ParticipantCompletedEventInfoMapper(ParticipantStartedEventInfoMapper participantEventInfoMapper) {
    public ParticipantCompletedEventInfoResponse toResponse(Event event, ParticipantInfo participantInfo) {
        return new ParticipantCompletedEventInfoResponse(participantEventInfoMapper.toResponse(event, participantInfo),
                participantInfo.calculateScore(),
                event.calculateMaxScore());
    }
}
