package com.shoparoo.exception;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super();
    }

    public WrongPasswordException(String message) {
        super(message);
    }
}
