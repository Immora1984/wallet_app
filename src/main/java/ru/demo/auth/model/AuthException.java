package ru.demo.auth.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponseException;

public class AuthException extends ErrorResponseException {

    public AuthException(HttpStatusCode status) {super(status);}

    public static class Failed extends AuthException {
        public Failed() { super(HttpStatus.BAD_REQUEST); }
    }
}
