package com.example.e_commerce.Producto.Exceptions;

public class NoMoreStockException extends RuntimeException{
    public NoMoreStockException(Long producto_id) {
        super("El producto con el id: "+producto_id+" se encuentra actualmente sin stock");
    }

    public NoMoreStockException() {
    }
}
