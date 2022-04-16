package ru.rsreu.contests_system.api.organization_creating_application.resource;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.rsreu.contests_system.api.organization_creating_application.exception.NotFoundOrganizationCreatingApplicationException;
import ru.rsreu.contests_system.api.organization_creating_application.exception.NotUniqueOrganizationInfoException;
import ru.rsreu.contests_system.api.organization_creating_application.resource.dto.not_unique_info.NotUniqueOrganizationInfoMapper;
import ru.rsreu.contests_system.api.organization_creating_application.resource.dto.not_unique_info.NotUniqueOrganizationInfoResponse;

@RestControllerAdvice
@AllArgsConstructor
public class OrganizationCreatingApplicationExceptionControllerAdvice extends ResponseEntityExceptionHandler {
    private final NotUniqueOrganizationInfoMapper notUniqueOrganizationInfoMapper;

    @ExceptionHandler(NotUniqueOrganizationInfoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<NotUniqueOrganizationInfoResponse> handleNotUniqueOrganizationInfoException(
            NotUniqueOrganizationInfoException exception) {
        return new ResponseEntity<>(notUniqueOrganizationInfoMapper.toResponse(exception), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundOrganizationCreatingApplicationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleNotFoundOrganizationCreatingApplicationException(
            NotFoundOrganizationCreatingApplicationException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
