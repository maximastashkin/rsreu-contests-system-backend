package ru.rsreu.contests_system.security.refresh;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.rsreu.contests_system.security.refresh.dto.RefreshTokenErrorResponse;
import ru.rsreu.contests_system.security.refresh.exception.BlackListTokenException;
import ru.rsreu.contests_system.security.refresh.exception.CompromisedTokenException;
import ru.rsreu.contests_system.security.refresh.exception.InvalidTokenException;
import ru.rsreu.contests_system.security.refresh.exception.RefreshTokenErrorStatus;

@RestControllerAdvice
public class RefreshControllerAdvice {
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<RefreshTokenErrorResponse> handleInvalidTokenException() {
        return formRefreshTokenErrorRequestResponseEntity(RefreshTokenErrorStatus.INVALID_TOKEN);
    }

    private ResponseEntity<RefreshTokenErrorResponse> formRefreshTokenErrorRequestResponseEntity(
            RefreshTokenErrorStatus status) {
        return new ResponseEntity<>(
                new RefreshTokenErrorResponse(status), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CompromisedTokenException.class)
    public ResponseEntity<RefreshTokenErrorResponse> handleCompromisedTokenException() {
        return formRefreshTokenErrorRequestResponseEntity(RefreshTokenErrorStatus.COMPROMISED);
    }

    @ExceptionHandler(BlackListTokenException.class)
    public ResponseEntity<RefreshTokenErrorResponse> handleBlackListTokenException() {
        return formRefreshTokenErrorRequestResponseEntity(RefreshTokenErrorStatus.BLACK_LIST);
    }
}
