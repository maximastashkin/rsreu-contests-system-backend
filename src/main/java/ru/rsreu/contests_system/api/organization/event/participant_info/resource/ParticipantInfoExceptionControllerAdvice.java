package ru.rsreu.contests_system.api.organization.event.participant_info.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.rsreu.contests_system.api.organization.event.participant_info.exception.ParticipantInfoNotFoundException;

@RestControllerAdvice
public class ParticipantInfoExceptionControllerAdvice {
    @ExceptionHandler(ParticipantInfoNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleParticipantInfoNotFoundException(ParticipantInfoNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
