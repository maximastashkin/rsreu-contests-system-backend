package ru.rsreu.contests_system.api.organization.event.resource.dto.started_event_info;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.organization.event.participant_info.ParticipantInfo;

@Component
public class StartedEventInfoMapper {
    public StartedEventInfoResponse toResponse(Event event, ParticipantInfo participantInfo) {
        return new StartedEventInfoResponse(
                event.getName(),
                event.getEventType(),
                event.getDescription(),
                participantInfo.getMaxEndDateTime(),
                participantInfo.getTasksSolutions().stream().map(
                        taskSolution -> taskSolution.getId().toString()).toList());
    }
}
