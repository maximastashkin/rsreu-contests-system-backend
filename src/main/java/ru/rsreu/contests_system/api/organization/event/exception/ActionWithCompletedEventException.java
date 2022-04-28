package ru.rsreu.contests_system.api.organization.event.exception;

public class ActionWithCompletedEventException extends RuntimeException {
    public ActionWithCompletedEventException(String message) {
        super(message);
    }
}
