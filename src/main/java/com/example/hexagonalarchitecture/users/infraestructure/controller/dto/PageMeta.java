package com.example.hexagonalarchitecture.users.infraestructure.controller.dto;

public record PageMeta(
    int page,
    int size,
    long totalElements,
    int totalPages
) {}