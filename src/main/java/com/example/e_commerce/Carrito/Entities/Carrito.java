package com.example.e_commerce.Carrito.Entities;

import com.example.e_commerce.User.Entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "carrito",cascade = CascadeType.ALL)
    private Set<ProductoCarrito> productosCarrito;

    public Carrito(User user) {
        this.user = user;
        this.productosCarrito = new HashSet<>();
    }

    public Double total(){
        Double total=0.0;
        for(ProductoCarrito item: productosCarrito){
            total+=item.getPrecioProducto();
        }
        return total;
    }

}
