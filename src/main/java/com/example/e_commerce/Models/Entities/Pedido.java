package com.example.e_commerce.Models.Entities;

import com.example.e_commerce.Models.Entities.Productos.ProductoPedido;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "pedido",cascade = CascadeType.ALL)
    private Set<ProductoPedido> productos;

    private LocalDateTime fecha;

    private Double total;
    public Pedido(Set<ProductoPedido>productos) {
        this.productos=productos;
        this.fecha = LocalDateTime.now();
        this.total=totalFactura();
    }

    public Pedido() {
        this.productos=new HashSet<>();
        this.fecha = LocalDateTime.now();
        this.total=0.0;
    }

    public Double totalFactura(){
        Double total=0.0;
        for(ProductoPedido item: productos){
            total+=item.getPrecioProducto();
        }
        return total;
    }

}
