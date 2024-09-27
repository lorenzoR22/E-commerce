package com.example.e_commerce.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacturaDTO {
    @NotBlank
    private Long id;
    @NotBlank
    private Set<ProductoCarritoDTO> productos;
    @NotBlank
    private String fecha;
    @NotBlank
    private Double total;
}
