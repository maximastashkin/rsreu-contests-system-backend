package ru.rsreu.contests_system.api.organization.event.exception;

public class ActionWithNonActualEventException extends RuntimeException {
    public ActionWithNonActualEventException(String message) {
        super(message);
    }
}
