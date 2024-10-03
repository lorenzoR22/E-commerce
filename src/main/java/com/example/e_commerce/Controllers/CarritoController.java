package com.example.e_commerce.Controllers;

import com.example.e_commerce.DTOs.CarritoDTO;
import com.example.e_commerce.DTOs.FacturaDTO;
import com.example.e_commerce.Exceptions.IdNotFound;
import com.example.e_commerce.Exceptions.NoMoreStock;
import com.example.e_commerce.Services.CarritoService;
import com.example.e_commerce.Services.ProductoCarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carrito")
public class CarritoController {
    @Autowired
    private CarritoService carritoService;

    @GetMapping("/getAll")
    public ResponseEntity<List<CarritoDTO>>getAllCarritos(){
        return ResponseEntity.status(HttpStatus.OK).body(carritoService.getAllCarritos());
    }
    @GetMapping("/getCarrito/{id}")
    public ResponseEntity<CarritoDTO>getCarrito(@PathVariable Long id) throws IdNotFound {
        return ResponseEntity.status(HttpStatus.OK).body(carritoService.getCarritoById(id));
    }

    @PostMapping("{id_carrito}/addProducto/{id_producto}")
    public ResponseEntity<CarritoDTO>addProductoCarrito(@PathVariable Long id_carrito,@PathVariable Long id_producto) throws NoMoreStock, IdNotFound {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(carritoService.addProductoCarrito(id_carrito,id_producto));
    }

    @PostMapping("/comprar/{id}")
    public ResponseEntity<FacturaDTO>comprarCarrito(@PathVariable Long id) throws IdNotFound {
        return ResponseEntity.status(HttpStatus.OK).body(carritoService.comprarCarrito(id));
    }

    @GetMapping("/facturas")
    public ResponseEntity <List<FacturaDTO>>getAllFacturas(){
        return ResponseEntity.status(HttpStatus.OK).body(carritoService.getAllFacturas());
    }
    @DeleteMapping("/deleteProducto/{id}")
    public ResponseEntity<Boolean>deleteProductoCarrito(@PathVariable Long id) throws IdNotFound {
        return ResponseEntity.status(HttpStatus.OK).body(carritoService.deleteProductoCarrito(id));
    }
}
