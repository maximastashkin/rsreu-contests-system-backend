package ru.rsreu.contests_system.api.organization.event.exception;

public class ActionWithNonFinishedEventException extends RuntimeException {
    public ActionWithNonFinishedEventException(String message) {
        super(message);
    }
}
