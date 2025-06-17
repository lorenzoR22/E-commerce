package com.example.e_commerce.Carrito.Entities;

import com.example.e_commerce.Producto.Entities.Producto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ProductoCarrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id",nullable = false)
    private Producto producto;

    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "carrito_id",nullable = false)
    private Carrito carrito;

    public ProductoCarrito(Producto producto,Carrito carrito) {
        this.producto = producto;
        this.cantidad = 1;
        this.carrito=carrito;
    }

    public Integer sumarCantidad(){
        return this.cantidad+=1;
    }

    public Integer restarCantidad(){
        if(this.cantidad>0){
            return this.cantidad-=1;
        }
        return 0;
    }

    public Double getPrecioProducto(){
        return this.cantidad*producto.getPrecio();
    }

}
