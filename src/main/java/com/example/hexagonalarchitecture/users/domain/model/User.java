package com.example.hexagonalarchitecture.users.domain.model;

import java.time.LocalDate;

public record User(
    Long id,
    String username,
    String firstName,
    String lastName,
    String email,
    String phone,
    String document,
    String address,
    UserStatus status,
    LocalDate birthDate
){
}