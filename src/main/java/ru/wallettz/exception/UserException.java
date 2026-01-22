package ru.wallettz.exception;

import org.hibernate.annotations.NotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponseException;

public class UserException extends ErrorResponseException {
    private UserException(HttpStatusCode status) {
        super(status);
    }

    public static class NotFound extends UserException {
        public NotFound() {
            super(HttpStatus.NOT_FOUND);
        }
    }
}
