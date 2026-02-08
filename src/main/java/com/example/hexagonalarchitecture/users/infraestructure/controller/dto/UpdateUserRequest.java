package com.example.hexagonalarchitecture.users.infraestructure.controller.dto;

import java.time.LocalDate;

import com.example.hexagonalarchitecture.users.domain.model.UserStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
    @NotBlank(message = "username es obligatorio")
    @Size(min = 3, max = 50, message = "username debe tener entre 3 y 50 caracteres")
    String username,
    @NotBlank(message = "firstName es obligatorio")
    String firstName,
    @NotBlank(message = "lastName es obligatorio")
    String lastName,
    @NotNull(message = "status es obligatorio")
    UserStatus status,
    @Email(message = "email debe ser válido")
    String email,
    String phone,
    String address,
    LocalDate birthDate
){
}
