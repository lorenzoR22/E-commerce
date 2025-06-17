package com.example.e_commerce.User.Exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
    }

    public UserNotFoundException(Long user_id) {
        super("No se encontro el user con el id: "+user_id);
    }
}
