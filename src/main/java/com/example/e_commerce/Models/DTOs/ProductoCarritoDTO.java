package com.example.e_commerce.Models.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductoCarritoDTO {
    @NotBlank
    private Long id;
    @NotBlank
    private ProductoDTO producto;

    private Integer cantidad;

}