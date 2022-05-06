package ru.rsreu.contests_system.api.organization.event.service.checking;

import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.user.User;

@FunctionalInterface
public interface ParticipantEventConditionChecker {
    void checkEventForCondition(Event event, User participant);
}
