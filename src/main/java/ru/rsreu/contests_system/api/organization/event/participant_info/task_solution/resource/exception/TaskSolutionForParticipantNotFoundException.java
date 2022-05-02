package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource.exception;

public class TaskSolutionForParticipantNotFoundException extends RuntimeException {
    public TaskSolutionForParticipantNotFoundException(String message) {
        super(message);
    }
}
