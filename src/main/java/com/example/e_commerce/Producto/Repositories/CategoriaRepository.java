package com.example.e_commerce.Producto.Repositories;

import com.example.e_commerce.Producto.Entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria,Long> {
    Optional<Categoria>findByNombre(String categoria);
}
