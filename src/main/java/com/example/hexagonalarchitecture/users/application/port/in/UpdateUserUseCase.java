package com.example.hexagonalarchitecture.users.application.port.in;

import com.example.hexagonalarchitecture.users.application.port.out.UserRepositoryPort;
import com.example.hexagonalarchitecture.users.application.port.out.UserWithPassword;
import com.example.hexagonalarchitecture.users.domain.model.User;

public class UpdateUserUseCase {

    private final UserRepositoryPort userRepository;

    public UpdateUserUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(Long id, User user) {        
        UserWithPassword existing = userRepository.findByIdWithPassword(id);
        
        User toUpdate = new User(
            id,
            user.username(),
            user.firstName(),
            user.lastName(),
            user.email(),
            user.phone(),
            user.document(),
            user.address(),
            user.status(),
            user.birthDate()
        );
        return userRepository.update(toUpdate, existing.passwordHash());
    }
}
