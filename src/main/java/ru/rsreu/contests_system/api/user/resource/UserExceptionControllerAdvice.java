package ru.rsreu.contests_system.api.user.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.rsreu.contests_system.api.user.exception.AdminBlockingAttemptException;
import ru.rsreu.contests_system.api.user.exception.NotUniqueEmailException;
import ru.rsreu.contests_system.api.user.exception.UserNotFoundException;

@ControllerAdvice
public class UserExceptionControllerAdvice {
    @ExceptionHandler(NotUniqueEmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> notUniqueEmailExceptionHandle(NotUniqueEmailException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> userNotFoundExceptionHandle(UserNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AdminBlockingAttemptException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<?> adminBlockingAttemptExceptionHandle(AdminBlockingAttemptException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
    }
}
