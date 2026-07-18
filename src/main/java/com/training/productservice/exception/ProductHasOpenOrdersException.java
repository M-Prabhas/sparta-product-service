package com.training.productservice.exception;

public class ProductHasOpenOrdersException extends RuntimeException {

    public ProductHasOpenOrdersException(String message) {
        super(message);
    }
}
