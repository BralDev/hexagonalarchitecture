package com.example.hexagonalarchitecture.users.application.port.out;

import com.example.hexagonalarchitecture.users.domain.model.User;

public record UserWithPassword(
    User user,
    String passwordHash
) {
}
