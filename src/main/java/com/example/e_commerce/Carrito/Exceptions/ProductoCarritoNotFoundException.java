package com.example.e_commerce.Carrito.Exceptions;

public class ProductoCarritoNotFoundException extends RuntimeException{
    public ProductoCarritoNotFoundException() {
    }

    public ProductoCarritoNotFoundException(Long productoCarrito_id) {
        super("No se encontro el producto de carrito con el id: "+productoCarrito_id);
    }
}
