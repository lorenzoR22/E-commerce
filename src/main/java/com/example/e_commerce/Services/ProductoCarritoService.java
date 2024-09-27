package com.example.e_commerce.Services;

import com.example.e_commerce.Entities.Productos.ProductoCarrito;
import com.example.e_commerce.Exceptions.IdNotFound;
import com.example.e_commerce.Repositories.ProductoCarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoCarritoService {
    @Autowired
    private ProductoCarritoRepository productoCarritoRepository;

    @Autowired ProductoService productoService;

    public ProductoCarrito saveProductoCarrito(ProductoCarrito productoCarrito){
        return productoCarritoRepository.save(productoCarrito);
    }

    public Boolean deleteProductoCarrito(Long id) throws IdNotFound {

        ProductoCarrito producto=productoCarritoRepository.findById(id)
                .orElseThrow(()->new IdNotFound());

        producto.getProducto().sumarStock();

        if (producto.getCantidad()>1){
            producto.restarCantidad();
        }else{
            productoCarritoRepository.deleteById(id);
        }
        productoService.updateProducto(producto.getProducto().getId(),productoService.productoToDTO(producto.getProducto()));
        return true;
    }
    public void comprarProductoCarrito(Long id){
        if(productoCarritoRepository.existsById(id)){
            productoCarritoRepository.deleteById(id);
        }
    }

}

