package com.example.e_commerce.Exceptions;

public class IdNotFound extends RuntimeException{
    public IdNotFound() {
    }
    public IdNotFound(String message) {
        super(message);
    }
}
