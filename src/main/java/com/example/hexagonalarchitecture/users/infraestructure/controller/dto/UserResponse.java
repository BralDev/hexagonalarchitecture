package com.example.hexagonalarchitecture.users.infraestructure.controller.dto;

import java.time.LocalDate;

import com.example.hexagonalarchitecture.users.domain.model.UserStatus;

public record UserResponse(
    Long id,
    String firstName,
    String lastName,
    LocalDate birthDate,
    UserStatus status
){
}