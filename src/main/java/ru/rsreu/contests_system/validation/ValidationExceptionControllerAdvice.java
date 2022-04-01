package ru.rsreu.contests_system.validation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.rsreu.contests_system.api.organization_creating_application.exception.NotUniqueOrganizationInfo;
import ru.rsreu.contests_system.api.user.exception.NotUniqueEmailException;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ValidationExceptionControllerAdvice {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("Not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotUniqueEmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> notUniqueEmailExceptionHandle(NotUniqueEmailException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotUniqueOrganizationInfo.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> notUniqueOrganizationEmail(NotUniqueOrganizationInfo exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
