package ru.rsreu.contests_system.api.organization.event.service.checking;

import org.springframework.stereotype.Component;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.user.User;

@Component
public record ParticipantPerformingEventConditionChecker(
        EventCheckerUtil eventCheckerUtil) implements ParticipantEventConditionChecker {
    @Override
    public void checkEventForCondition(Event event, User participant) {
        eventCheckerUtil.checkNonActual(event);
        eventCheckerUtil.checkNonStartedByParticipant(event, participant);
        eventCheckerUtil.checkCompletedByParticipant(event, participant);
    }
}
