package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.rsreu.contests_system.api.organization.event.participant_info.task_solution.exception.TaskSolutionForParticipantNotFoundException;

@RestControllerAdvice
public class TaskResourceExceptionControllerAdvice {
    @ExceptionHandler(value = TaskSolutionForParticipantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<?> handleTaskSolutionForParticipantNotFoundException(
            TaskSolutionForParticipantNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }
}
