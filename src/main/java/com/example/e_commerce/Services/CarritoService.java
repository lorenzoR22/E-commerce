package com.example.e_commerce.Services;

import com.example.e_commerce.Models.DTOs.CarritoDTO;
import com.example.e_commerce.Models.DTOs.ProductoCarritoDTO;
import com.example.e_commerce.Models.Entities.Carrito;
import com.example.e_commerce.Models.Entities.Productos.Producto;
import com.example.e_commerce.Models.Entities.Productos.ProductoCarrito;
import com.example.e_commerce.Exceptions.IdNotFound;
import com.example.e_commerce.Exceptions.NoMoreStock;
import com.example.e_commerce.Repositories.CarritoRepository;
import com.example.e_commerce.Repositories.ProductoRepository;
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
    @Transactional
    public CarritoDTO addProductoCarrito(Long id_carrito,Long id_producto) throws NoMoreStock, IdNotFound {
        Carrito carrito=carritoRepository.findById(id_carrito)
                .orElseThrow(IdNotFound::new);

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
                .orElseThrow(IdNotFound::new);
        return carritoToDTO(carrito);
    }
}
