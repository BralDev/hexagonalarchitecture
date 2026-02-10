package com.example.hexagonalarchitecture.users.domain.model;

import java.time.LocalDate;

public record User(
    String id,
    String username,
    String firstName,
    String lastName,
    String email,
    String phone,
    DocumentType documentType,
    String documentNumber,
    String address,
    UserStatus status,
    LocalDate birthDate
){
}