package com.example.e_commerce.Carrito.Repositories;

import com.example.e_commerce.Carrito.Entities.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarritoRepository extends JpaRepository<Carrito,Long> {
}
