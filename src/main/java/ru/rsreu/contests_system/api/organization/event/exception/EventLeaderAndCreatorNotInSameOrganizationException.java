package ru.rsreu.contests_system.api.organization.event.exception;

public class EventLeaderAndCreatorNotInSameOrganizationException extends RuntimeException {
    public EventLeaderAndCreatorNotInSameOrganizationException(String message) {
        super(message);
    }
}
