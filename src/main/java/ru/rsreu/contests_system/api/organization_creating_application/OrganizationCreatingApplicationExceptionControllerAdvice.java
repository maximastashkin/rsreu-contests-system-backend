package ru.rsreu.contests_system.api.organization_creating_application;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.rsreu.contests_system.api.organization_creating_application.exception.NotUniqueOrganizationInfo;

@RestControllerAdvice
public class OrganizationCreatingApplicationExceptionControllerAdvice {
    @ExceptionHandler(NotUniqueOrganizationInfo.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> notUniqueOrganizationEmail(NotUniqueOrganizationInfo exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
