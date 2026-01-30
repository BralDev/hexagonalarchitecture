package com.example.hexagonal_architecture_example.users.infraestructure.controller.dto;

import java.time.LocalDate;

import com.example.hexagonal_architecture_example.users.domain.model.UserStatus;

public record UserReponse (
    Long id,
    String firstName,
    String lastName,
    LocalDate birthDate,
    UserStatus status
){
}