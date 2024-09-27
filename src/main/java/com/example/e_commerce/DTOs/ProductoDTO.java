package com.example.e_commerce.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {

        @NotBlank
        private Long id;

        @NotBlank
        private String nombre;

        @NotBlank
        private String descripcion;

        @NotBlank
        private String categoria;

        @NotBlank
        private Set<String> imagenes;

        @NotBlank
        private Integer stock;

        @NotBlank
        private Double precio;


}
