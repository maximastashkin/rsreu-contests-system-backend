package ru.rsreu.contests_system.api.organization.event.exception;

public class NotUniqueEventNameException extends RuntimeException {
    public NotUniqueEventNameException(String message) {
        super(message);
    }
}
