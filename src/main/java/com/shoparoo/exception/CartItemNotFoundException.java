package com.shoparoo.exception;

public class CartItemNotFoundException extends RuntimeException {
    public CartItemNotFoundException() {
        super();
    }

    public CartItemNotFoundException(String message) {
        super(message);
    }
}
