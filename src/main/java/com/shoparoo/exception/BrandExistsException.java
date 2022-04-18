package com.shoparoo.exception;

public class BrandExistsException extends RuntimeException {
    public BrandExistsException() {
        super();
    }

    public BrandExistsException(String message) {
        super(message);
    }
}
