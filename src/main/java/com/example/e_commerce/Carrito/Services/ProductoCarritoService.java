package com.example.e_commerce.Carrito.Services;

import com.example.e_commerce.Carrito.Entities.ProductoCarrito;
import com.example.e_commerce.Carrito.Exceptions.ProductoCarritoNotFoundException;
import com.example.e_commerce.Carrito.Repositories.ProductoCarritoRepository;
import com.example.e_commerce.Producto.Exceptions.ProductoNotFoundException;
import com.example.e_commerce.Producto.Services.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductoCarritoService {

    private final ProductoCarritoRepository productoCarritoRepository;

    private final ProductoService productoService;

    public Boolean deleteProductoCarrito(Long id) throws ProductoCarritoNotFoundException, ProductoNotFoundException {

        ProductoCarrito producto=productoCarritoRepository.findById(id)
                .orElseThrow(()->new ProductoCarritoNotFoundException(id));

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

