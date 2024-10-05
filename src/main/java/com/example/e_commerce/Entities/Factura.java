package com.example.e_commerce.Entities;

import com.example.e_commerce.Entities.Productos.ProductoFactura;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "facturas")
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "factura",cascade = CascadeType.ALL)
    private Set<ProductoFactura> productos;

    private LocalDateTime fecha;

    private Double total;
    //cuando se hace la factura se resta el stock de los productos.
    public Factura(Set<ProductoFactura>productos) {
        this.productos=productos;
        this.fecha = LocalDateTime.now();
        this.total=totalFactura();
    }

    public Factura() {
        this.productos=new HashSet<>();
        this.fecha = LocalDateTime.now();
        this.total=0.0;
    }

    public Double totalFactura(){
        Double total=0.0;
        for(ProductoFactura item: productos){
            total+=item.getPrecioProducto();
        }
        return total;
    }

}
