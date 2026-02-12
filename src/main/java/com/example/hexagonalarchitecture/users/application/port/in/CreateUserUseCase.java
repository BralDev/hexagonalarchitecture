package com.example.hexagonalarchitecture.users.application.port.in;

import java.time.LocalDate;
import java.time.Period;

import com.example.hexagonalarchitecture.users.application.port.out.UserRepositoryPort;
import com.example.hexagonalarchitecture.users.domain.model.User;
import com.example.hexagonalarchitecture.users.domain.model.UserStatus;
import com.example.hexagonalarchitecture.users.infraestructure.exception.InvalidDocumentException;
import com.example.hexagonalarchitecture.users.infraestructure.exception.ValidationException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Caso de uso para crear nuevos usuarios en el sistema.
 * <p>
 * Responsabilidades:
 * - Validar formato del número de documento según su tipo (DNI, CE, PASSPORT, TI)
 * - Validar unicidad de username, email y número de documento
 * - Validar edad mínima de 18 años
 * - Hashear la contraseña con BCrypt antes de persistir
 * - Asignar estado ACTIVE por defecto
 * <p>
 * Excepciones lanzadas:
 * - {@link InvalidDocumentException} si el formato del documento es inválido
 * - {@link ValidationException} si username/email/documento está duplicado o edad < 18
 */
public class CreateUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateUserUseCase(UserRepositoryPort userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Crea un nuevo usuario aplicando todas las validaciones de negocio.
     * 
     * @param user datos del usuario a crear (sin ID, será generado automáticamente)
     * @param rawPassword contraseña en texto plano (será hasheada antes de persistir)
     * @return usuario creado con ID generado y estado ACTIVE
     * @throws InvalidDocumentException si el formato del documento no coincide con su tipo
     * @throws ValidationException si username/email/documento duplicado o edad menor a 18 años
     */
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
        
        // Validar que el username no esté duplicado
        if (userRepository.existsByUsername(user.username())) {
            throw new ValidationException("El username '" + user.username() + "' ya está en uso");
        }
        
        // Validar que el email no esté duplicado
        if (user.email() != null && userRepository.existsByEmail(user.email())) {
            throw new ValidationException("El email '" + user.email() + "' ya está registrado");
        }
        
        // Validar que el documento no esté duplicado
        if (user.documentNumber() != null && userRepository.existsByDocumentNumber(user.documentNumber())) {
            throw new ValidationException("Ya existe un usuario con el documento " + user.documentNumber());
        }
        
        // Validar edad mínima de 18 años
        if (user.birthDate() != null) {
            int age = Period.between(user.birthDate(), LocalDate.now()).getYears();
            if (age < 18) {
                throw new ValidationException("El usuario debe ser mayor de 18 años. Edad actual: " + age + " años");
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