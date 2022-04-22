package ru.rsreu.contests_system.api.organization.event.resource.dto.event_info;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.organization.event.participant_info.ParticipantInfo;
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
                getFollowingSign(event, candidateForEventChecking)
        );
    }

    private boolean getFollowingSign(Event event, Optional<User> candidate) {
        return candidate.isPresent() && event.getParticipantsInfos()
                .stream()
                .map(ParticipantInfo::getParticipant)
                .toList().contains(candidate.get());
    }
}
