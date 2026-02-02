package com.example.hexagonalarchitecture.users.application.port.in;

import com.example.hexagonalarchitecture.users.application.port.out.UserRepositoryPort;
import com.example.hexagonalarchitecture.users.domain.model.User;
import com.example.hexagonalarchitecture.users.domain.model.UserStatus;

public class DeactivateUserUseCase {

    private final UserRepositoryPort userRepository;

    public DeactivateUserUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        
        User deactivatedUser = new User(
                user.id(),
                user.firstName(),
                user.lastName(),
                UserStatus.INACTIVE,
                user.birthDate());
        return userRepository.save(deactivatedUser);
    }
}
