package ru.rsreu.contests_system.api.organization.event.exception;

public class EventWrongDatesOrderException extends RuntimeException {
    public EventWrongDatesOrderException(String message) {
        super(message);
    }
}
