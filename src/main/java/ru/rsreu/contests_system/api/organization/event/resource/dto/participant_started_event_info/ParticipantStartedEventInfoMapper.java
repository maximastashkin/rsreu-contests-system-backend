package ru.rsreu.contests_system.api.organization.event.resource.dto.participant_started_event_info;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.organization.event.participant_info.ParticipantInfo;

@Component
public class ParticipantStartedEventInfoMapper {
    public ParticipantStartedEventInfoResponse toResponse(Event event, ParticipantInfo participantInfo) {
        return new ParticipantStartedEventInfoResponse(
                event.getName(),
                event.getEventType(),
                event.getDescription(),
                participantInfo.getMaxEndDateTime(),
                participantInfo.getTasksSolutions().stream().map(
                        taskSolution -> taskSolution.getId().toString()).toList());
    }
}
