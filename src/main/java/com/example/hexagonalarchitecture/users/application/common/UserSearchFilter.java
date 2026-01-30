package com.example.hexagonalarchitecture.users.application.common;

import java.time.LocalDate;

import com.example.hexagonalarchitecture.users.domain.model.UserStatus;

public record UserSearchFilter(
        String firstName,
        String lastName,
        UserStatus status,
        LocalDate birthDateFrom,
        LocalDate birthDateTo
) {}