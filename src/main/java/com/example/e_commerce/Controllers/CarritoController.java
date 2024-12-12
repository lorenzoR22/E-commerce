package com.example.e_commerce.Controllers;

import com.example.e_commerce.Models.DTOs.CarritoDTO;
import com.example.e_commerce.Models.DTOs.PedidoDTO;
import com.example.e_commerce.Exceptions.IdNotFound;
import com.example.e_commerce.Exceptions.NoMoreStock;
import com.example.e_commerce.Services.CarritoService;
import com.example.e_commerce.Services.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    private final PedidoService pedidoService;

    @GetMapping("/getAll")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<CarritoDTO>getAllCarritos(){
        return carritoService.getAllCarritos();
    }

    @GetMapping("/getCarrito/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CarritoDTO getCarrito(@PathVariable Long id) throws IdNotFound {
        return carritoService.getCarritoById(id);
    }

    @PostMapping("{id_carrito}/addProducto/{id_producto}")
    @ResponseStatus(HttpStatus.OK)
    public CarritoDTO addProductoCarrito(@PathVariable Long id_carrito,@PathVariable Long id_producto) throws NoMoreStock, IdNotFound {
        return carritoService.addProductoCarrito(id_carrito,id_producto);
    }

    @PostMapping("/comprar/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String comprarCarrito(@PathVariable Long id) throws IdNotFound {
        return pedidoService.comprarCarrito(id);
    }

    @GetMapping("/pedidos")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<PedidoDTO>getAllPedidos(){
        return pedidoService.getAllPedidos();
    }

    @PostMapping("/checkPago/{idCarrito}")
    public ResponseEntity<String>checkPago(@RequestBody Map<String, Object> payload,@PathVariable("idCarrito") Long idCarrito){
        return pedidoService.handleNotification(payload, idCarrito);
    }

    @DeleteMapping("/deleteProducto/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Boolean deleteProductoCarrito(@PathVariable Long id) throws IdNotFound {
        return carritoService.deleteProductoCarrito(id);
    }
}
