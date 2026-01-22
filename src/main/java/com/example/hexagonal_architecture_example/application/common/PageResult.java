package com.example.hexagonal_architecture_example.application.common;

import java.util.List;

public record PageResult<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {}