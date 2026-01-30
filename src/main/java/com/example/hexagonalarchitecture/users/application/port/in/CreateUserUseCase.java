package com.example.hexagonalarchitecture.users.application.port.in;

import com.example.hexagonalarchitecture.users.application.port.out.UserRepositoryPort;
import com.example.hexagonalarchitecture.users.domain.model.User;
import com.example.hexagonalarchitecture.users.domain.model.UserStatus;

public class CreateUserUseCase {

    private final UserRepositoryPort userRepository;

    public CreateUserUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(User user) {
        User toSave = new User(
                null,
                user.firstName(),
                user.lastName(),
                user.status() == null ? UserStatus.ACTIVE : user.status(),
                user.birthDate()
            );
        return userRepository.save(toSave);
    }
}
