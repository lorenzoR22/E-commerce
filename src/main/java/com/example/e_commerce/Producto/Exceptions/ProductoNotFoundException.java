package com.example.e_commerce.Producto.Exceptions;

public class ProductoNotFoundException extends RuntimeException{
    public ProductoNotFoundException() {
    }

    public ProductoNotFoundException(Long producto_id) {
        super("No se encontro el producto con el id: "+producto_id);
    }
}
