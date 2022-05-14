package ru.rsreu.contests_system.api.organization.event.exception;

public class NotAvailableForOrganizerEventType extends RuntimeException {
    public NotAvailableForOrganizerEventType(String message) {
        super(message);
    }
}
