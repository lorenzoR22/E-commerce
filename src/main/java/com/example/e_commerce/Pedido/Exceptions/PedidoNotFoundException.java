package com.example.e_commerce.Pedido.Exceptions;

public class PedidoNotFoundException extends RuntimeException{
    public PedidoNotFoundException() {
    }

    public PedidoNotFoundException(Long id) {
        super("No se encontro el pedido con el id: "+id);
    }
}
