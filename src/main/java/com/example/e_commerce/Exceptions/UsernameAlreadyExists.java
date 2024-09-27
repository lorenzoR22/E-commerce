package com.example.e_commerce.Exceptions;

public class UsernameAlreadyExists extends Exception{
    public UsernameAlreadyExists(String message) {
        super(message);
    }

    public UsernameAlreadyExists() {
    }
}
