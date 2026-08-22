package com.shopsphere.ecommerce.exception;

public class CartValidationException extends RuntimeException {

    public CartValidationException(String message) {
        super(message);
    }
}