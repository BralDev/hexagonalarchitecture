package com.example.hexagonalarchitecture.users.application.port.in;

import com.example.hexagonalarchitecture.users.application.port.out.UserRepositoryPort;
import com.example.hexagonalarchitecture.users.domain.model.User;
import com.example.hexagonalarchitecture.users.domain.model.UserStatus;
import com.example.hexagonalarchitecture.users.infraestructure.exception.InvalidDocumentException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CreateUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateUserUseCase(UserRepositoryPort userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User execute(User user, String rawPassword) {
        // Validar formato del número de documento
        if (user.documentType() != null && user.documentNumber() != null) {
            if (!user.documentType().isValidNumber(user.documentNumber())) {
                throw new InvalidDocumentException(
                    "El número de documento no cumple con el formato esperado para " 
                    + user.documentType().getDescription()
                );
            }
        }

        String hashedPassword = passwordEncoder.encode(rawPassword);
        User toCreate = new User(
                null,
                user.username(),
                user.firstName(),
                user.lastName(),
                user.email(),
                user.phone(),
                user.documentType(),
                user.documentNumber(),
                user.address(),
                user.status() == null ? UserStatus.ACTIVE : user.status(),
                user.birthDate());
        return userRepository.create(toCreate, hashedPassword);
    }
}