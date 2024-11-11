package com.example.e_commerce.Services;

import com.example.e_commerce.Models.Entities.Productos.ProductoCarrito;
import com.example.e_commerce.Exceptions.IdNotFound;
import com.example.e_commerce.Repositories.ProductoCarritoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductoCarritoService {

    private final ProductoCarritoRepository productoCarritoRepository;

    private final ProductoService productoService;

    public Boolean deleteProductoCarrito(Long id) throws IdNotFound {

        ProductoCarrito producto=productoCarritoRepository.findById(id)
                .orElseThrow(IdNotFound::new);

        producto.getProducto().sumarStock();

        if (producto.getCantidad()>1){
            producto.restarCantidad();
        }else{
            productoCarritoRepository.deleteById(id);
        }
        productoService.updateProducto(producto.getProducto().getId(),productoService.productoToDTO(producto.getProducto()));
        return true;
    }


}

