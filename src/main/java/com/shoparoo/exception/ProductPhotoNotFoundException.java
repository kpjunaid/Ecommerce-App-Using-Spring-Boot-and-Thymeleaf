package com.shoparoo.exception;

public class ProductPhotoNotFoundException extends RuntimeException {
    public ProductPhotoNotFoundException() {
        super();
    }

    public ProductPhotoNotFoundException(String message) {
        super(message);
    }
}
