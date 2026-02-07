package com.example.hexagonalarchitecture.users.application.port.in;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.hexagonalarchitecture.users.application.port.out.UserRepositoryPort;
import com.example.hexagonalarchitecture.users.domain.model.User;

public class ChangePasswordUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;

    public ChangePasswordUseCase(UserRepositoryPort userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User execute(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        String hashedPassword = passwordEncoder.encode(newPassword);

        return userRepository.update(user, hashedPassword);
    }
}
