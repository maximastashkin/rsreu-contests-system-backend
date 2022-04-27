package ru.rsreu.contests_system.api.organization.event.exception;

public class ParticipantInfoNotFoundException extends RuntimeException {
    public ParticipantInfoNotFoundException(String message) {
        super(message);
    }
}
