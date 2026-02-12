package com.example.hexagonalarchitecture.users.application.port.in;

import com.example.hexagonalarchitecture.users.application.port.out.UserRepositoryPort;
import com.example.hexagonalarchitecture.users.application.port.out.UserWithPassword;
import com.example.hexagonalarchitecture.users.domain.model.User;

/**
 * Caso de uso para actualizar datos de usuarios existentes.
 * <p>
 * Comportamiento:
 * - Actualiza todos los campos permitidos del usuario
 * - Preserva la contraseña hasheada existente (no se modifica)
 * - El tipo y número de documento NO pueden cambiar (requiere recrear usuario)
 * <p>
 * Excepciones lanzadas:
 * - {@link com.example.hexagonalarchitecture.users.infraestructure.exception.EntityNotFoundException} si el usuario no existe
 */
public class UpdateUserUseCase {

    private final UserRepositoryPort userRepository;

    public UpdateUserUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Actualiza los datos de un usuario preservando su contraseña actual.
     * 
     * @param id identificador UUID del usuario a actualizar
     * @param user datos actualizados del usuario
     * @return usuario actualizado
     * @throws com.example.hexagonalarchitecture.users.infraestructure.exception.EntityNotFoundException si no existe
     */
    public User execute(String id, User user) {
        UserWithPassword existing = userRepository.findByIdWithPassword(id);

        User toUpdate = new User(
                id,
                user.username(),
                user.firstName(),
                user.lastName(),
                user.email(),
                user.phone(),
                user.documentType(),
                user.documentNumber(),
                user.address(),
                user.status(),
                user.birthDate());
        return userRepository.update(toUpdate, existing.passwordHash());
    }
}