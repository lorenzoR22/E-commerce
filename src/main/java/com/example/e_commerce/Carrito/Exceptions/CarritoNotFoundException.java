package com.example.e_commerce.Carrito.Exceptions;

public class CarritoNotFoundException extends RuntimeException{
    public CarritoNotFoundException() {
    }

    public CarritoNotFoundException(Long carrito_id) {
        super("No se encontro el carrito con el id: "+carrito_id);
    }
}
