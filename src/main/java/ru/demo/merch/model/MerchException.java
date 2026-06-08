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

    public static class UploadImageException extends MerchException {
        public UploadImageException() {super(HttpStatus.BAD_REQUEST);}
    }

    public static class PhotosLimit extends MerchException {
        public PhotosLimit() { super(HttpStatus.BAD_REQUEST); }
    }

    public static class RemovePhotoException extends MerchException {
        public RemovePhotoException() { super(HttpStatus.BAD_REQUEST); }
    }
}
