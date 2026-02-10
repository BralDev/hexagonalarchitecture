package com.example.hexagonalarchitecture.users.application.port.in;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.hexagonalarchitecture.users.application.port.out.UserRepositoryPort;
import com.example.hexagonalarchitecture.users.application.port.out.UserWithPassword;
import com.example.hexagonalarchitecture.users.domain.model.User;
import com.example.hexagonalarchitecture.users.infraestructure.exception.InvalidPasswordException;

public class ChangePasswordUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;

    public ChangePasswordUseCase(UserRepositoryPort userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User execute(String id, String currentPassword, String newPassword, String confirmPassword) {
        UserWithPassword existing = userRepository.findByIdWithPassword(id);
        User user = existing.user();

        if (!passwordEncoder.matches(currentPassword, existing.passwordHash())) {
            throw new InvalidPasswordException("La contraseña actual es incorrecta");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new InvalidPasswordException("La nueva contraseña y la confirmación no coinciden");
        }

        String hashedPassword = passwordEncoder.encode(newPassword);

        return userRepository.update(user, hashedPassword);
    }
}
