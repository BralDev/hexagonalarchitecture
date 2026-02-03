package com.example.hexagonalarchitecture.users.application.port.in;

import com.example.hexagonalarchitecture.users.application.port.out.UserRepositoryPort;
import com.example.hexagonalarchitecture.users.application.port.out.UserWithPassword;
import com.example.hexagonalarchitecture.users.domain.model.User;
import com.example.hexagonalarchitecture.users.domain.model.UserStatus;

public class DeleteUserUseCase {

    private final UserRepositoryPort userRepository;

    public DeleteUserUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(Long id) {
        UserWithPassword existing = userRepository.findByIdWithPassword(id);
        User user = existing.user();
        
        User deletedUser = new User(
            user.id(),
            user.username(),
            user.firstName(),
            user.lastName(),
            user.email(),
            user.phone(),
            user.document(),
            user.address(),
            UserStatus.DELETED,
            user.birthDate()
        );
        userRepository.update(deletedUser, existing.passwordHash());
    }
}
