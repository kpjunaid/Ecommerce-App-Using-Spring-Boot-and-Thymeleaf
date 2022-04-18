package com.shoparoo.exception;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException() {
        super();
    }

    public TokenNotFoundException(String message) {
        super(message);
    }
}
