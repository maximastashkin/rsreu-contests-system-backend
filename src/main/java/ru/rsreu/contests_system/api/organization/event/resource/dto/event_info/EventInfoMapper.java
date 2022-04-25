package ru.rsreu.contests_system.api.organization.event.resource.dto.event_info;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.user.User;

import java.util.Optional;

@Component
public record EventInfoMapper() {
    public EventInfoResponse toResponse(Event event, Optional<User> candidateForEventChecking) {
        return new EventInfoResponse(
                event.getId().toString(),
                event.getName(),
                event.getEventType(),
                event.getDescription(),
                event.getStartDateTime(),
                event.getEndDateTime(),
                getParticipantStatus(event, candidateForEventChecking)
        );
    }

    private ParticipantStatus getParticipantStatus(Event event, Optional<User> candidate) {
        ParticipantStatus result = ParticipantStatus.NONE;
        if (candidate.isPresent()) {
            User participant = candidate.get();
            if (event.isParticipantCompletedEvent(participant)) {
                result = ParticipantStatus.COMPLETED;
            } else if (event.isParticipantStartedEvent(participant)) {
                result = ParticipantStatus.STARTED;
            } else if (event.isParticipantFollowedOnEvent(participant)) {
                result = ParticipantStatus.FOLLOWED;
            }
        }
        return result;
    }
}
