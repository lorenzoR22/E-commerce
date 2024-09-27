package com.example.e_commerce.Entities.Productos;

import com.example.e_commerce.Entities.Factura;
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
public class ProductoFactura {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id",nullable = false)
    private Producto producto;

    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "factura_id",nullable = false)
    private Factura factura;

    public ProductoFactura(Producto producto, Integer cantidad, Factura factura) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.factura = factura;
    }

    public Double getPrecioProducto(){
        return this.cantidad*producto.getPrecio();
    }


}
