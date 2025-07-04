package com.example.e_commerce.Pedido.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    @NotBlank
    private Set<ProductoPedidoDTO> productos;
    @NotBlank
    private String fecha;
    @NotNull
    private Double total;
}
