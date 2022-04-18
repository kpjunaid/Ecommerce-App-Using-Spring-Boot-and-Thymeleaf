package com.shoparoo.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException() {
        super();
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }
}
