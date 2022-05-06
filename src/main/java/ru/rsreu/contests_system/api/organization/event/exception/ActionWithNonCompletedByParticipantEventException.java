package ru.rsreu.contests_system.api.organization.event.exception;

public class ActionWithNonCompletedByParticipantEventException extends RuntimeException {
    public ActionWithNonCompletedByParticipantEventException(String message) {
        super(message);
    }
}
