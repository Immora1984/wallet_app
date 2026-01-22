package ru.wallettz.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponseException;

public class WalletException extends ErrorResponseException {
    private WalletException(HttpStatusCode status) {
        super(status);
    }

    public static class NotFound extends WalletException {
        public NotFound() { super(HttpStatus.NOT_FOUND);}
    }

    public static class NotEnoughBalance extends WalletException {
        public NotEnoughBalance() { super(HttpStatus.BAD_REQUEST);}
    }
}