package com.shoparoo.exception;

public class ProductPhotoLimitException extends RuntimeException {
    public ProductPhotoLimitException() {
        super();
    }

    public ProductPhotoLimitException(String message) {
        super(message);
    }
}
