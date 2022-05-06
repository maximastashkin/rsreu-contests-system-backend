package ru.rsreu.contests_system.api.organization.event.service.checking;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.user.User;

@Component
public record ParticipantCompletingEventConditionChecker(EventCheckerUtil eventCheckerUtil)
        implements ParticipantEventConditionChecker {
    @Override
    public void checkEventForCondition(Event event, User participant) {
        eventCheckerUtil.checkNonCompletedByParticipant(event, participant);
        eventCheckerUtil.checkFinished(event);
    }
}
