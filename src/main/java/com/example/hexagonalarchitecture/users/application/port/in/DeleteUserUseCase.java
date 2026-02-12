package com.example.hexagonalarchitecture.users.application.port.in;

import com.example.hexagonalarchitecture.users.application.port.out.UserRepositoryPort;
import com.example.hexagonalarchitecture.users.application.port.out.UserWithPassword;
import com.example.hexagonalarchitecture.users.domain.model.User;
import com.example.hexagonalarchitecture.users.domain.model.UserStatus;
import com.example.hexagonalarchitecture.users.infraestructure.exception.ValidationException;

/**
 * Caso de uso para eliminar usuarios del sistema.
 * <p>
 * Implementa eliminación LÓGICA (soft delete):
 * - El usuario NO se elimina físicamente de la base de datos
 * - Solo cambia su estado a DELETED
 * - El usuario no aparecerá en búsquedas por estado ACTIVE
 * - Los datos históricos se preservan
 * <p>
 * Reglas de negocio:
 * - No se puede eliminar al usuario administrador del sistema (username "admin")
 * <p>
 * Excepciones lanzadas:
 * - {@link com.example.hexagonalarchitecture.users.infraestructure.exception.EntityNotFoundException} si ID no existe
 * - {@link ValidationException} si se intenta eliminar al usuario administrador
 */
public class DeleteUserUseCase {

    private final UserRepositoryPort userRepository;

    public DeleteUserUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Elimina lógicamente un usuario cambiando su estado a DELETED.
     * 
     * @param id identificador UUID del usuario a eliminar
     * @throws com.example.hexagonalarchitecture.users.infraestructure.exception.EntityNotFoundException si el usuario no existe
     * @throws ValidationException si se intenta eliminar al usuario administrador
     */
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