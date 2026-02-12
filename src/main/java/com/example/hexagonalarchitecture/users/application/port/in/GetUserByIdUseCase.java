package com.example.hexagonalarchitecture.users.application.port.in;

import com.example.hexagonalarchitecture.users.application.port.out.UserRepositoryPort;
import com.example.hexagonalarchitecture.users.domain.model.User;
import com.example.hexagonalarchitecture.users.infraestructure.exception.EntityNotFoundException;

/**
 * Caso de uso para obtener un usuario por su ID.
 */
public class GetUserByIdUseCase {
    
    private final UserRepositoryPort userRepository;

    public GetUserByIdUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Busca un usuario por su identificador UUID.
     * 
     * @param id identificador del usuario
     * @return usuario encontrado
     * @throws EntityNotFoundException si el usuario no existe
     */
    public User execute(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));
    }
}
