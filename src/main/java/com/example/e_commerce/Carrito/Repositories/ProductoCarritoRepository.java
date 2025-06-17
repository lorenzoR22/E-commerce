package com.example.e_commerce.Carrito.Repositories;

import com.example.e_commerce.Carrito.Entities.Carrito;
import com.example.e_commerce.Carrito.Entities.ProductoCarrito;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoCarritoRepository extends JpaRepository<ProductoCarrito,Long> {
    public void deleteByCarrito(Carrito carrito);
}
