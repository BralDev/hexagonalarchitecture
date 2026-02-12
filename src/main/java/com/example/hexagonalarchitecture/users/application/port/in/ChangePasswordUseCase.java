package com.example.hexagonalarchitecture.users.application.port.in;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.hexagonalarchitecture.users.application.port.out.UserRepositoryPort;
import com.example.hexagonalarchitecture.users.application.port.out.UserWithPassword;
import com.example.hexagonalarchitecture.users.domain.model.User;
import com.example.hexagonalarchitecture.users.infraestructure.exception.InvalidPasswordException;

/**
 * Caso de uso para cambiar la contraseña de un usuario.
 * <p>
 * Proceso de cambio de contraseña:
 * 1. Verifica que el usuario exista
 * 2. Valida que la contraseña actual proporcionada coincida con la almacenada (hasheada)
 * 3. Valida que la nueva contraseña y su confirmación coincidan
 * 4. Hashea la nueva contraseña con BCrypt
 * 5. Actualiza el usuario con la nueva contraseña hasheada
 * <p>
 * Excepciones lanzadas:
 * - {@link com.example.hexagonalarchitecture.users.infraestructure.exception.EntityNotFoundException} si el usuario no existe
 * - {@link InvalidPasswordException} si la contraseña actual es incorrecta o newPassword != confirmPassword
 */
public class ChangePasswordUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;

    public ChangePasswordUseCase(UserRepositoryPort userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cambia la contraseña de un usuario después de validar la contraseña actual.
     * 
     * @param id identificador UUID del usuario
     * @param currentPassword contraseña actual en texto plano (se verifica contra el hash almacenado)
     * @param newPassword nueva contraseña en texto plano (será hasheada antes de persistir)
     * @param confirmPassword confirmación de la nueva contraseña (debe coincidir con newPassword)
     * @return usuario actualizado con la nueva contraseña hasheada
     * @throws com.example.hexagonalarchitecture.users.infraestructure.exception.EntityNotFoundException si el usuario no existe
     * @throws InvalidPasswordException si currentPassword es incorrecta o newPassword != confirmPassword
     */
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
