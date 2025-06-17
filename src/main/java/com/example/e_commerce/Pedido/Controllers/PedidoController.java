package com.example.e_commerce.Pedido.Controllers;

import com.example.e_commerce.Carrito.Exceptions.CarritoNotFoundException;
import com.example.e_commerce.Pedido.DTOs.PedidoDTO;
import com.example.e_commerce.Pedido.Services.PedidoService;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pedido")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public PedidoDTO getPedido(@PathVariable Long id){
        return pedidoService.getPedido(id);
    }

    @PostMapping("/checkPago/{idCarrito}")
    public ResponseEntity<String> checkPago(@RequestBody Map<String, Object> payload, @PathVariable("idCarrito") Long idCarrito) throws MPException, MPApiException, CarritoNotFoundException {
        return pedidoService.handleNotification(payload, idCarrito);
    }

}
