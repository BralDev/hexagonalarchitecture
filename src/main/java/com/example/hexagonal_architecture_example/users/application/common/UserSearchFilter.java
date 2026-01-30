package com.example.hexagonal_architecture_example.users.application.common;

import java.time.LocalDate;

import com.example.hexagonal_architecture_example.users.domain.model.UserStatus;

public record UserSearchFilter(
        String firstName,
        String lastName,
        UserStatus status,
        LocalDate birthDateFrom,
        LocalDate birthDateTo
) {}