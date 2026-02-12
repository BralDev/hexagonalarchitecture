package com.example.hexagonalarchitecture.users.application.port.in;

import com.example.hexagonalarchitecture.users.application.port.out.UserRepositoryPort;
import com.example.hexagonalarchitecture.users.application.port.out.UserWithPassword;
import com.example.hexagonalarchitecture.users.domain.model.User;
import com.example.hexagonalarchitecture.users.domain.model.UserStatus;
import com.example.hexagonalarchitecture.users.infraestructure.exception.ValidationException;

public class DeleteUserUseCase {

    private final UserRepositoryPort userRepository;

    public DeleteUserUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(String id) {
        UserWithPassword existing = userRepository.findByIdWithPassword(id);
        User user = existing.user();
        
        // Proteger al usuario administrador del sistema
        if ("admin".equals(user.username())) {
            throw new ValidationException("No se puede eliminar al usuario administrador del sistema");
        }

        User deletedUser = new User(
                user.id(),
                user.username(),
                user.firstName(),
                user.lastName(),
                user.email(),
                user.phone(),
                user.documentType(),
                user.documentNumber(),
                user.address(),
                UserStatus.DELETED,
                user.birthDate());
        userRepository.update(deletedUser, existing.passwordHash());
    }
}