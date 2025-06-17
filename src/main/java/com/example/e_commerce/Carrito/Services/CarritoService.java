package com.example.e_commerce.Carrito.Services;

import com.example.e_commerce.Carrito.DTOs.CarritoDTO;
import com.example.e_commerce.Carrito.Entities.Carrito;
import com.example.e_commerce.Carrito.Exceptions.CarritoNotFoundException;
import com.example.e_commerce.Carrito.Entities.ProductoCarrito;
import com.example.e_commerce.Carrito.DTOs.ProductoCarritoDTO;
import com.example.e_commerce.Carrito.Exceptions.ProductoCarritoNotFoundException;
import com.example.e_commerce.Carrito.Repositories.CarritoRepository;
import com.example.e_commerce.Producto.Exceptions.ProductoNotFoundException;
import com.example.e_commerce.Producto.Entities.Producto;
import com.example.e_commerce.Producto.Exceptions.NoMoreStockException;
import com.example.e_commerce.Producto.Repositories.ProductoRepository;
import com.example.e_commerce.Producto.Services.ProductoService;
import com.example.e_commerce.User.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritoRepository;

    private final ProductoCarritoService productoCarritoService;

    private final UserService userService;

    private final ProductoService productoService;

    private final ProductoRepository productoRepository;

    public List<CarritoDTO>getAllCarritos(){
        List<Carrito>carritos=carritoRepository.findAll();
        return carritos.stream()
                .map(this::carritoToDTO
                ).toList();
    }

    @Transactional
    public CarritoDTO addProductoCarrito(Long id_carrito,Long id_producto) throws NoMoreStockException, CarritoNotFoundException, ProductoNotFoundException {
        Carrito carrito=carritoRepository.findById(id_carrito)
                .orElseThrow(()->new CarritoNotFoundException(id_carrito));

        Producto producto=productoService.getProducto(id_producto);
        validarStock(producto);
        producto.restarStock();
        obtenerOcrearProductoCarrito(carrito,producto);

        productoRepository.save(producto);
        carrito=carritoRepository.save(carrito);

        return carritoToDTO(carrito);
    }

    private void validarStock(Producto producto) throws NoMoreStockException {
        if(producto.getStock()<1){
            throw new NoMoreStockException(producto.getId());
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

    public Boolean deleteProductoCarrito(Long id_productoCarrito) throws ProductoCarritoNotFoundException, ProductoNotFoundException {
        return productoCarritoService.deleteProductoCarrito(id_productoCarrito);
    }

    public CarritoDTO getCarritoById(Long id) throws  CarritoNotFoundException {
        Carrito carrito=carritoRepository.findById(id)
                .orElseThrow(()->new CarritoNotFoundException(id));
        return carritoToDTO(carrito);
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
}
