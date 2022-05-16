package ru.rsreu.contests_system.api.organization.exception;

public class NotOrganizationLeaderException extends RuntimeException {
    public NotOrganizationLeaderException(String message) {
        super(message);
    }
}
