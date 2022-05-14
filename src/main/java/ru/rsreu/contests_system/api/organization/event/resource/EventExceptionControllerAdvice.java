package ru.rsreu.contests_system.api.organization.event.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.rsreu.contests_system.api.organization.event.exception.*;

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

    @ExceptionHandler(ParticipantInfoNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleParticipantInfoNotFoundException(ParticipantInfoNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ActionWithNonStartedEventException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<?> handleActionWithNonStartedEventException(ActionWithNonStartedEventException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ActionWithNonStartedByParticipantEventException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<?> handleActionWithNonStartedByParticipantEventException
            (ActionWithNonStartedByParticipantEventException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ActionWithCompletedEventException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<?> handleActionWithCompletedEventException
            (ActionWithCompletedEventException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ActionWithNonCompletedByParticipantEventException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<?> handleActionWithNonCompletedByParticipantEventException(
            ActionWithNonCompletedByParticipantEventException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ActionWithNonFinishedEventException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<?> handleActionWithNonFinishedEventException(ActionWithNonFinishedEventException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EventWrongDatesOrderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleEventWrongDatesOrderException(EventWrongDatesOrderException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotAvailableForOrganizerEventType.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<?> handleNotAvailableForOrganizerEventType(NotAvailableForOrganizerEventType exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EventLeaderAndCreatorNotInSameOrganizationException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<?> handleEventLeaderAndCreatorNotInSameOrganizationException(
            EventLeaderAndCreatorNotInSameOrganizationException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(AppointmentOrganizationLeaderAsEventLeaderException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<?> handleAppointmentOrganizationLeaderEventLeaderException(
            AppointmentOrganizationLeaderAsEventLeaderException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotUniqueEventNameException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleNotUniqueEventNameException(NotUniqueEventNameException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
