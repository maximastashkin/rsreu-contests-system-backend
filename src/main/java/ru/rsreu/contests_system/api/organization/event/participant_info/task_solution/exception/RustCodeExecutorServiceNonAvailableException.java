package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.exception;

public class RustCodeExecutorServiceNonAvailableException extends RuntimeException {
    public RustCodeExecutorServiceNonAvailableException(String message) {
        super(message);
    }
}