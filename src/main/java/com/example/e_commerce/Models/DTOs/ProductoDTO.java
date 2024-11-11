package com.example.e_commerce.Models.DTOs;

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
public class ProductoDTO {


        private Long id;

        @NotBlank
        private String nombre;

        @NotBlank
        private String descripcion;

        @NotBlank
        private String categoria;

        private Set<String> imagenes;

        @NotNull
        private Integer stock;

        @NotNull
        private Double precio;


}
