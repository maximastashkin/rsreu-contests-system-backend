package ru.rsreu.contests_system.api.user.exception;

public class AdminBlockingAttemptException extends RuntimeException {
    public AdminBlockingAttemptException(String message) {
        super(message);
    }
}
