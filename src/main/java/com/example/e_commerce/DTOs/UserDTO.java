package com.example.e_commerce.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @NotNull
    private Long id;

    @NotBlank
    @Size(max = 30,message = "El username no puede tener mas de 30 caracteres.")
    private String username;

    @NotBlank
    private String password;

    @Email(message = "Email invalido.")
    private String email;

    @NotBlank
    private String telefono;

    @NotNull
    private Set<String> roles;

}
