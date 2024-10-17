package com.example.e_commerce.DTOs;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CarritoDTO {
    @NotNull
    private Long id;
    @NotBlank
    private UserDTO user;
    @NotNull
    private Set<ProductoCarritoDTO> productosCarrito;

}
