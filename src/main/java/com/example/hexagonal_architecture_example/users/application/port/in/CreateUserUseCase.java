package com.example.hexagonal_architecture_example.users.application.port.in;

import com.example.hexagonal_architecture_example.users.application.port.out.UserRepositoryPort;
import com.example.hexagonal_architecture_example.users.domain.model.User;
import com.example.hexagonal_architecture_example.users.domain.model.UserStatus;

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
