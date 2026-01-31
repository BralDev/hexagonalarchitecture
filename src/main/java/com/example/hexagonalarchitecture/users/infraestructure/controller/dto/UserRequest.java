package com.example.hexagonalarchitecture.users.infraestructure.controller.dto;

import java.time.LocalDate;

import com.example.hexagonalarchitecture.users.domain.model.UserStatus;

public record UserRequest (
    String firstName,
    String lastName,
    LocalDate birthDate,
    UserStatus status
){
}