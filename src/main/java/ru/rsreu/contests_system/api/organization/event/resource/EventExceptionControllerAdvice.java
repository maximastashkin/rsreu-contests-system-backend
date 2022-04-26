package ru.rsreu.contests_system.api.organization.event.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.rsreu.contests_system.api.organization.event.exception.ActionWithNonActualEventException;
import ru.rsreu.contests_system.api.organization.event.exception.EventNotFoundException;
import ru.rsreu.contests_system.api.organization.event.exception.UserFollowingException;

@RestControllerAdvice
public class EventExceptionControllerAdvice {
    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleEventNotFoundException(EventNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserFollowingException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<?> handleUserFollowingException(UserFollowingException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ActionWithNonActualEventException.class)
    @ResponseStatus(HttpStatus.GONE)
    public ResponseEntity<?> handleActionWithNonActualEventException(ActionWithNonActualEventException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.GONE);
    }
}