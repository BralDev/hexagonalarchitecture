package com.example.hexagonalarchitecture.users.application.port.in;

import com.example.hexagonalarchitecture.users.application.port.out.UserRepositoryPort;
import com.example.hexagonalarchitecture.users.application.port.out.UserWithPassword;
import com.example.hexagonalarchitecture.users.domain.model.User;
import com.example.hexagonalarchitecture.users.domain.model.UserStatus;

public class ActivateUserUseCase {

    private final UserRepositoryPort userRepository;

    public ActivateUserUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(Long id) {
        UserWithPassword existing = userRepository.findByIdWithPassword(id);
        User user = existing.user();
        
        User activatedUser = new User(
            user.id(),
            user.username(),
            user.firstName(),
            user.lastName(),
            user.email(),
            user.phone(),
            user.document(),
            user.address(),
            UserStatus.ACTIVE,
            user.birthDate()
        );

        return userRepository.update(activatedUser, existing.passwordHash());
    }
}
