package ru.rsreu.contests_system.api.organization.event.participant_info.exception;

public class ParticipantInfoNotFoundException extends RuntimeException {
    public ParticipantInfoNotFoundException(String message) {
        super(message);
    }
}
