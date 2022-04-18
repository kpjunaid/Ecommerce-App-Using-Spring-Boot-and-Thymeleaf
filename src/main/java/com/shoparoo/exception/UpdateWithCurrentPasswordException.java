package com.shoparoo.exception;

public class UpdateWithCurrentPasswordException extends RuntimeException {
    public UpdateWithCurrentPasswordException() {
        super();
    }

    public UpdateWithCurrentPasswordException(String message) {
        super(message);
    }
}
