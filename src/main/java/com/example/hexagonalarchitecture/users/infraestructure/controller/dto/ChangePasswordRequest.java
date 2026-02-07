package com.example.hexagonalarchitecture.users.infraestructure.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
    @NotBlank(message = "currentPassword es obligatorio")
    String currentPassword,
    @NotBlank(message = "newPassword es obligatorio")
    @Size(min = 8, message = "newPassword debe tener al menos 8 caracteres")
    String newPassword,
    @NotBlank(message = "confirmPassword es obligatorio")
    String confirmPassword
) {
}
