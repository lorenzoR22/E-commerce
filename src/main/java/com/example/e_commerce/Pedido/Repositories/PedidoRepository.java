package com.example.e_commerce.Pedido.Repositories;

import com.example.e_commerce.Pedido.Entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido,Long> {
}
