package com.example.e_commerce.Producto.Repositories;

import com.example.e_commerce.Producto.Entities.ProductoImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoImgRepository extends JpaRepository<ProductoImg,Long> {

}
