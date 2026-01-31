package com.example.hexagonalarchitecture.users.infraestructure.controller.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
    @NotBlank(message = "firstName es obligatorio")
    String firstName,
    @NotBlank(message = "lastName es obligatorio")
    String lastName,
    LocalDate birthDate
){
}
