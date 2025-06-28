package com.example.e_commerce.Carrito.Controllers;

import com.example.e_commerce.Carrito.DTOs.CarritoDTO;
import com.example.e_commerce.Carrito.Exceptions.CarritoNotFoundException;
import com.example.e_commerce.Carrito.Services.CarritoService;
import com.example.e_commerce.Carrito.Exceptions.ProductoCarritoNotFoundException;
import com.example.e_commerce.Producto.Exceptions.ProductoNotFoundException;
import com.example.e_commerce.Producto.Exceptions.NoMoreStockException;
import com.example.e_commerce.Pedido.Services.PedidoService;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;
    private final PedidoService pedidoService;

    @GetMapping("/getCarrito/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CarritoDTO getCarrito(@PathVariable Long id) throws CarritoNotFoundException {
        return carritoService.getCarritoById(id);
    }

    @PostMapping("{id_carrito}/addProducto/{id_producto}")
    @ResponseStatus(HttpStatus.OK)
    public CarritoDTO addProductoCarrito(@PathVariable Long id_carrito,@PathVariable Long id_producto) throws NoMoreStockException, CarritoNotFoundException, ProductoNotFoundException {
        return carritoService.addProductoCarrito(id_carrito,id_producto);
    }

    @GetMapping("/comprar/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String comprarCarrito(@PathVariable Long id) throws MPException, MPApiException, CarritoNotFoundException {
        return pedidoService.comprarCarrito(id);
    }

    @DeleteMapping("/deleteProducto/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Boolean deleteProductoCarrito(@PathVariable Long id) throws ProductoCarritoNotFoundException, ProductoNotFoundException {
        return carritoService.deleteProductoCarrito(id);
    }
}
