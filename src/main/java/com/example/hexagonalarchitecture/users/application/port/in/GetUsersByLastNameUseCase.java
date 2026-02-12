package com.example.hexagonalarchitecture.users.application.port.in;

import com.example.hexagonalarchitecture.users.application.common.PageResult;
import com.example.hexagonalarchitecture.users.application.common.SortDirection;
import com.example.hexagonalarchitecture.users.application.common.UserSearchFilter;
import com.example.hexagonalarchitecture.users.application.common.UserSortField;
import com.example.hexagonalarchitecture.users.application.port.out.UserRepositoryPort;
import com.example.hexagonalarchitecture.users.domain.model.User;
import com.example.hexagonalarchitecture.users.domain.model.UserStatus;

/**
 * Caso de uso para buscar usuarios por apellido con filtros y paginación.
 * <p>
 * Aplica valores por defecto:
 * - status: ACTIVE si no se especifica
 * - page: 0 si es negativo
 * - size: 10 (min: 1, max: 100)
 * - sortField: LAST_NAME si no se especifica
 * - direction: ASC si no se especifica
 */
public class GetUsersByLastNameUseCase {

    private final UserRepositoryPort userRepository;

    public GetUsersByLastNameUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Busca usuarios por apellido con paginación y filtros opcionales.
     * 
     * @param lastName apellido a buscar (búsqueda parcial con LIKE)
     * @param status filtro por estado (default: ACTIVE)
     * @param page número de página (default: 0)
     * @param size tamaño de página (default: 10, max: 100)
     * @param sortField campo de ordenamiento (default: LAST_NAME)
     * @param direction dirección de ordenamiento (default: ASC)
     * @return resultado paginado de usuarios
     */
    public PageResult<User> execute(
            String lastName,
            UserStatus status,
            int page,
            int size,
            UserSortField sortField,
            SortDirection direction) {

        UserStatus resolvedStatus = status != null ? status : UserStatus.ACTIVE;

        UserSearchFilter filter = new UserSearchFilter(lastName, null, resolvedStatus, null, null);
        
        int resolvedPage = Math.max(page, 0);

        int resolvedSize = size > 0 ? Math.min(size, 100) : 10;

        UserSortField resolvedSortField = sortField != null ? sortField : UserSortField.LAST_NAME;

        SortDirection resolvedDirection = direction != null ? direction : SortDirection.ASC;

        return userRepository.search(
                filter,
                resolvedPage,
                resolvedSize,
                resolvedSortField,
                resolvedDirection);
    }
}