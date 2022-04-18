package com.shoparoo.exception;

public class CategoryExistsException extends RuntimeException {
    public CategoryExistsException() {
        super();
    }

    public CategoryExistsException(String message) {
        super(message);
    }
}
