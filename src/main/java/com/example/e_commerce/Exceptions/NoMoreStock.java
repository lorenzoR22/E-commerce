package com.example.e_commerce.Exceptions;

public class NoMoreStock extends Exception{
    public NoMoreStock(String message) {
        super(message);
    }

    public NoMoreStock() {
    }
}
