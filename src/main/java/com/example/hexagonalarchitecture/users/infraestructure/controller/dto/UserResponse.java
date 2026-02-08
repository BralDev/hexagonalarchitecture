package com.example.hexagonalarchitecture.users.infraestructure.controller.dto;

import java.time.LocalDate;

import com.example.hexagonalarchitecture.users.domain.model.DocumentType;
import com.example.hexagonalarchitecture.users.domain.model.UserStatus;

public record UserResponse(
    Long id,
    String username,
    String firstName,
    String lastName,
    String email,
    String phone,
    DocumentType documentType,
    String documentNumber,
    String address,
    LocalDate birthDate,
    UserStatus status
){
}