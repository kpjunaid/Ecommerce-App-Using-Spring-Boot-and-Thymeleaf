package com.shoparoo.exception;

public class CountryNotFoundException extends RuntimeException {
    public CountryNotFoundException() {
        super();
    }

    public CountryNotFoundException(String message) {
        super(message);
    }
}
