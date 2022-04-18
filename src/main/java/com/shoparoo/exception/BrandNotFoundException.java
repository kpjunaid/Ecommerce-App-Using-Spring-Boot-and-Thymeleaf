package com.shoparoo.exception;

public class BrandNotFoundException extends RuntimeException {
    public BrandNotFoundException() {
        super();
    }

    public BrandNotFoundException(String message) {
        super(message);
    }
}
