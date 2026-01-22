package com.example.hexagonal_architecture_example.infraestructure.controller.dto;

public record PageMeta(
    int page,
    int size,
    long totalElements,
    int totalPages
) {}