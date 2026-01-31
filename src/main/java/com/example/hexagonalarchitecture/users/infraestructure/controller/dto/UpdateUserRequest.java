package com.example.hexagonalarchitecture.users.infraestructure.controller.dto;

import java.time.LocalDate;

import com.example.hexagonalarchitecture.users.domain.model.UserStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRequest(
    @NotBlank(message = "firstName es obligatorio")
    String firstName,
    @NotBlank(message = "lastName es obligatorio")
    String lastName,
    @NotNull(message = "status es obligatorio")
    UserStatus status,
    LocalDate birthDate
){
}
