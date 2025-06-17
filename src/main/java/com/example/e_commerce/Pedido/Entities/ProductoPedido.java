package com.example.e_commerce.Pedido.Entities;

import com.example.e_commerce.Producto.Entities.Producto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProductoPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id",nullable = false)
    private Producto producto;

    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    public ProductoPedido(Producto producto, Pedido pedido) {
        this.producto = producto;
        this.cantidad = 1;
        this.pedido = pedido;
    }

    public ProductoPedido(Producto producto, Integer cantidad, Pedido pedido) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.pedido =pedido;
    }

    public Double getPrecioProducto(){
        return this.cantidad*producto.getPrecio();
    }
}
