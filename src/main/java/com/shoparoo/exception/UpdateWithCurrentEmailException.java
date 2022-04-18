package com.shoparoo.exception;

public class UpdateWithCurrentEmailException extends RuntimeException {
    public UpdateWithCurrentEmailException() {
        super();
    }

    public UpdateWithCurrentEmailException(String message) {
        super(message);
    }
}
