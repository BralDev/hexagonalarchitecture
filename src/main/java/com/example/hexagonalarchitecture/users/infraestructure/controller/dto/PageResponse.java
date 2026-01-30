package com.example.hexagonalarchitecture.users.infraestructure.controller.dto;

import java.util.List;

public record PageResponse<T>(
        List<T> data,
        PageMeta meta
) {}