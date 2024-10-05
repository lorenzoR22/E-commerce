package com.example.e_commerce.Services;

import com.example.e_commerce.DTOs.CarritoDTO;
import com.example.e_commerce.DTOs.FacturaDTO;
import com.example.e_commerce.DTOs.ProductoCarritoDTO;
import com.example.e_commerce.Entities.*;
import com.example.e_commerce.Entities.Productos.Producto;
import com.example.e_commerce.Entities.Productos.ProductoCarrito;
import com.example.e_commerce.Exceptions.IdNotFound;
import com.example.e_commerce.Exceptions.NoMoreStock;
import com.example.e_commerce.Repositories.CarritoRepository;
import com.example.e_commerce.Repositories.FacturaRepository;
import com.example.e_commerce.Repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CarritoService {
    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoCarritoService productoCarritoService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private FacturaRepository facturaRepository;

    public List<CarritoDTO>getAllCarritos(){
        List<Carrito>carritos=carritoRepository.findAll();
        return carritos.stream()
                .map(carrito->carritoToDTO(carrito)
                ).toList();
    }

    private void validarStock(Producto producto) throws NoMoreStock {
        if(producto.getStock()<1){
            throw new NoMoreStock("No hay stock disponible para este producto");
        }
    }

    private void obtenerOcrearProductoCarrito(Carrito carrito, Producto producto){
        carrito.getProductosCarrito().stream()
                .filter(product -> product.getProducto().getId().equals(producto.getId()))
                .findFirst()
                .map(pc -> {
                    pc.sumarCantidad();
                    return pc;
                })
                .orElseGet(() -> {
                    ProductoCarrito pc = new ProductoCarrito(producto, carrito);
                    carrito.getProductosCarrito().add(pc);
                    return pc;
                });
    }

    public CarritoDTO addProductoCarrito(Long id_carrito,Long id_producto) throws NoMoreStock, IdNotFound {
        Carrito carrito=carritoRepository.findById(id_carrito)
                .orElseThrow(()->new IdNotFound());

        Producto producto=productoService.getProducto(id_producto);
        validarStock(producto);
        producto.restarStock();
        obtenerOcrearProductoCarrito(carrito,producto);

        productoRepository.save(producto);
        carrito=carritoRepository.save(carrito);

        return carritoToDTO(carrito);
    }

    public Boolean deleteProductoCarrito(Long id_productoCarrito) throws IdNotFound {
        return productoCarritoService.deleteProductoCarrito(id_productoCarrito);
    }

    public CarritoDTO carritoToDTO(Carrito carrito){
        return new CarritoDTO(
                carrito.getId(),
                userService.userToDTO(carrito.getUser()),
                carrito.getProductosCarrito().stream()
                        .map(product->new ProductoCarritoDTO(
                                product.getId(),
                                productoService.productoToDTO(product.getProducto()),
                                product.getCantidad()))
                        .collect(Collectors.toSet())
        );
    }

    public CarritoDTO getCarritoById(Long id) throws IdNotFound {
        Carrito carrito=carritoRepository.findById(id)
                .orElseThrow(()->new IdNotFound());
        return carritoToDTO(carrito);
    }


}
