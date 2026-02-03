package com.example.hexagonalarchitecture.users.infraestructure.controller.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
    @NotBlank(message = "username es obligatorio")
    @Size(min = 3, max = 50, message = "username debe tener entre 3 y 50 caracteres")
    String username,
    @NotBlank(message = "password es obligatorio")
    @Size(min = 8, message = "password debe tener al menos 8 caracteres")
    String password,
    @NotBlank(message = "firstName es obligatorio")
    String firstName,
    @NotBlank(message = "lastName es obligatorio")
    String lastName,
    @Email(message = "email debe ser válido")
    String email,
    String phone,
    String document,
    String address,
    LocalDate birthDate
){
}
