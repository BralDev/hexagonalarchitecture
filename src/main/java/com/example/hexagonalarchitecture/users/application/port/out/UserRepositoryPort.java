package com.example.hexagonalarchitecture.users.application.port.out;

import java.util.Optional;

import com.example.hexagonalarchitecture.users.application.common.PageResult;
import com.example.hexagonalarchitecture.users.application.common.SortDirection;
import com.example.hexagonalarchitecture.users.application.common.UserSearchFilter;
import com.example.hexagonalarchitecture.users.application.common.UserSortField;
import com.example.hexagonalarchitecture.users.domain.model.User;

/**
 * Puerto de salida para operaciones de persistencia de usuarios.
 * <p>
 * Define el contrato entre la capa de aplicación y la infraestructura de persistencia,
 * siguiendo el patrón hexagonal (ports & adapters). Esta interfaz permite que la lógica
 * de negocio no dependa de detalles de implementación (JPA, base de datos específica, etc.).
 * <p>
 * Responsabilidades:
 * - CRUD de usuarios con passwords hasheadas
 * - Búsqueda y filtrado con paginación
 * - Validación de unicidad (username, email, documento)
 */
public interface UserRepositoryPort {
    
    /**
     * Crea un nuevo usuario en el sistema.
     * 
     * @param user datos del usuario a crear (sin ID)
     * @param hashedPassword contraseña ya hasheada con BCrypt
     * @return usuario creado con ID generado por la base de datos
     */
    User create(User user, String hashedPassword);
    
    /**
     * Actualiza un usuario existente.
     * El ID del usuario debe estar presente y existir en la base de datos.
     * 
     * @param user datos actualizados del usuario (con ID)
     * @param passwordHash contraseña hasheada (puede ser la misma o nueva)
     * @return usuario actualizado
     */
    User update(User user, String passwordHash);
    
    /**
     * Busca un usuario por su ID.
     * Retorna solo los datos públicos del usuario (sin password).
     * 
     * @param id identificador UUID del usuario
     * @return Optional con el usuario si existe, Optional.empty() si no existe
     */
    Optional<User> findById(String id);
    
    /**
     * Busca un usuario por ID incluyendo su password hasheada.
     * Útil para operaciones que requieren verificar o actualizar la contraseña.
     * 
     * @param id identificador UUID del usuario
     * @return UserWithPassword con datos del usuario y password hash
     * @throws com.example.hexagonalarchitecture.users.infraestructure.exception.EntityNotFoundException si no existe
     */
    UserWithPassword findByIdWithPassword(String id);
    
    /**
     * Busca usuarios con filtros, paginación y ordenamiento.
     * Permite filtrar por múltiples criterios: apellido, documento, estado, rango de fechas.
     * 
     * @param filter criterios de búsqueda (todos opcionales)
     * @param page número de página (0-indexed)
     * @param size tamaño de página
     * @param sortField campo por el cual ordenar
     * @param direction dirección de ordenamiento (ASC/DESC)
     * @return resultado paginado con usuarios encontrados y metadatos de paginación
     */
    PageResult<User> search(
        UserSearchFilter filter,
        int page,
        int size,
        UserSortField sortField,
        SortDirection direction);
    
    /**
     * Verifica si existe un usuario con el username dado.
     * Útil para validar duplicados antes de crear/actualizar usuarios.
     * 
     * @param username nombre de usuario a verificar
     * @return true si existe, false si está disponible
     */
    boolean existsByUsername(String username);
    
    /**
     * Verifica si existe un usuario con el email dado.
     * Útil para validar duplicados antes de crear/actualizar usuarios.
     * 
     * @param email dirección de correo electrónico a verificar
     * @return true si existe, false si está disponible
     */
    boolean existsByEmail(String email);
    
    /**
     * Verifica si existe un usuario con el número de documento dado.
     * Útil para validar duplicados antes de crear usuarios.
     * 
     * @param documentNumber número de documento de identidad a verificar
     * @return true si existe, false si está disponible
     */
    boolean existsByDocumentNumber(String documentNumber);
}