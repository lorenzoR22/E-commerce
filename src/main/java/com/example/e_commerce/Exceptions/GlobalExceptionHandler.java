package com.example.e_commerce.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String>handleGenericException(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrio un error inesperado:"+e.getMessage());
    }
    @ExceptionHandler(IdNotFound.class)
    public ResponseEntity<String>idNotFoundException(IdNotFound e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se encontro el id "+e.getMessage());
    }
    @ExceptionHandler(NoMoreStock.class)
    public ResponseEntity<String>noMoreStockException(NoMoreStock e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El producto no tiene mas stock "+e.getMessage());
    }
    @ExceptionHandler(UsernameAlreadyExists.class)
    public ResponseEntity<String>usernameAlreadyExistsException(UsernameAlreadyExists e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Este usuario ya existe "+e.getMessage());
    }
}
