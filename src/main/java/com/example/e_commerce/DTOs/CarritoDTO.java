package com.example.e_commerce.DTOs;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private Long id;
    @NotBlank
    private UserDTO user;
    @NotBlank
    private Set<ProductoCarritoDTO> productosCarrito;

}
