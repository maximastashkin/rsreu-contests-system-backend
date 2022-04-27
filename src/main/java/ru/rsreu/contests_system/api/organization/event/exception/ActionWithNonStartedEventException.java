package ru.rsreu.contests_system.api.organization.event.exception;

public class ActionWithNonStartedEventException extends RuntimeException {
    public ActionWithNonStartedEventException(String message) {
        super(message);
    }
}
