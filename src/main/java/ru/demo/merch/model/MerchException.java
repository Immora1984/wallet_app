package ru.demo.merch.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponseException;

public class MerchException extends ErrorResponseException {
    public MerchException(HttpStatusCode code) {
        super(code);
    }

    public static class NotFound extends MerchException {
        public NotFound() { super(HttpStatus.NOT_FOUND); }
    }
}
