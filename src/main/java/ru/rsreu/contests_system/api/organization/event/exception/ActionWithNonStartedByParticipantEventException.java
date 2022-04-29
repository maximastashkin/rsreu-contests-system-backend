package ru.rsreu.contests_system.api.organization.event.exception;

public class ActionWithNonStartedByParticipantEventException extends RuntimeException {
    public ActionWithNonStartedByParticipantEventException(String message) {
        super(message);
    }
}
