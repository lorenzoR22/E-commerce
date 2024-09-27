package com.example.e_commerce.Repositories;

import com.example.e_commerce.Entities.Productos.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto,Long> {
}
