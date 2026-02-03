package com.example.hexagonalarchitecture.users.application.port.in;

import com.example.hexagonalarchitecture.users.application.port.out.UserRepositoryPort;
import com.example.hexagonalarchitecture.users.domain.model.User;
import com.example.hexagonalarchitecture.users.domain.model.UserStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CreateUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateUserUseCase(UserRepositoryPort userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User execute(User user, String rawPassword) {
        String hashedPassword = passwordEncoder.encode(rawPassword);
        User toCreate = new User(
                null,
                user.username(),
                user.firstName(),
                user.lastName(),
                user.email(),
                user.phone(),
                user.document(),
                user.address(),
                user.status() == null ? UserStatus.ACTIVE : user.status(),
                user.birthDate()
            );
        return userRepository.create(toCreate, hashedPassword);
    }
}
