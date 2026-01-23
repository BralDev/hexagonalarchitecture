package com.example.hexagonal_architecture_example.application.common;

public record UserSearchFilter(
        String firstName,
        String lastName
) {}