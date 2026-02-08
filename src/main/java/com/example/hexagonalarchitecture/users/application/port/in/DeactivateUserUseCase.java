package com.example.hexagonalarchitecture.users.application.port.in;

import com.example.hexagonalarchitecture.users.application.port.out.UserRepositoryPort;
import com.example.hexagonalarchitecture.users.application.port.out.UserWithPassword;
import com.example.hexagonalarchitecture.users.domain.model.User;
import com.example.hexagonalarchitecture.users.domain.model.UserStatus;

public class DeactivateUserUseCase {

    private final UserRepositoryPort userRepository;

    public DeactivateUserUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(Long id) {
        UserWithPassword existing = userRepository.findByIdWithPassword(id);
        User user = existing.user();

        User deactivatedUser = new User(
                user.id(),
                user.username(),
                user.firstName(),
                user.lastName(),
                user.email(),
                user.phone(),
                user.documentType(),
                user.documentNumber(),
                user.address(),
                UserStatus.INACTIVE,
                user.birthDate());
        return userRepository.update(deactivatedUser, existing.passwordHash());
    }
}