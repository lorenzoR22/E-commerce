package com.example.e_commerce.Repositories;

import com.example.e_commerce.Models.Entities.Carrito;
import com.example.e_commerce.Models.Entities.Productos.ProductoCarrito;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoCarritoRepository extends JpaRepository<ProductoCarrito,Long> {
    public void deleteByCarrito(Carrito carrito);
}
