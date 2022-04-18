package com.shoparoo.exception;

public class CartItemExistsException extends RuntimeException {
    public CartItemExistsException() {
        super();
    }

    public CartItemExistsException(String message) {
        super(message);
    }
}
