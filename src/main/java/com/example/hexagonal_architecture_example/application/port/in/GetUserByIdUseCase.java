package com.example.hexagonal_architecture_example.application.port.in;

import com.example.hexagonal_architecture_example.application.port.out.UserRepositoryPort;
import com.example.hexagonal_architecture_example.domain.model.User;

public class GetUserByIdUseCase {
    
    private final UserRepositoryPort userRepository;

    public GetUserByIdUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
