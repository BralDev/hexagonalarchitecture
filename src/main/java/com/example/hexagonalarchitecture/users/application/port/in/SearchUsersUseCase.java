package com.example.hexagonalarchitecture.users.application.port.in;

import com.example.hexagonalarchitecture.users.application.common.PageResult;
import com.example.hexagonalarchitecture.users.application.common.SortDirection;
import com.example.hexagonalarchitecture.users.application.common.UserSearchFilter;
import com.example.hexagonalarchitecture.users.application.common.UserSortField;
import com.example.hexagonalarchitecture.users.application.port.out.UserRepositoryPort;
import com.example.hexagonalarchitecture.users.domain.model.User;

/**
 * Caso de uso para búsqueda avanzada de usuarios con múltiples criterios.
 * <p>
 * Permite filtrar por:
 * - lastName (búsqueda parcial)
 * - documentNumber (búsqueda parcial)
 * - status (ACTIVE, INACTIVE, DELETED)
 * - birthDateFrom y birthDateTo (rango de fechas)
 * <p>
 * Aplica valores por defecto:
 * - page: 0 si es negativo
 * - size: 10 (min: 1, max: 100)
 * - sortField: ID si no se especifica
 * - direction: ASC si no se especifica
 */
public class SearchUsersUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public SearchUsersUseCase(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    /**
     * Realiza búsqueda avanzada de usuarios con múltiples criterios opcionales.
     * 
     * @param filter criterios de búsqueda (todos opcionales)
     * @param page número de página (default: 0)
     * @param size tamaño de página (default: 10, max: 100)
     * @param sortField campo de ordenamiento (default: ID)
     * @param direction dirección de ordenamiento (default: ASC)
     * @return resultado paginado de usuarios que cumplen los criterios
     */
    public PageResult<User> execute(
            UserSearchFilter filter,
            int page,
            int size,
            UserSortField sortField,
            SortDirection direction) {

        int resolvedPage = Math.max(page, 0);

        int resolvedSize = size > 0 ? Math.min(size, 100) : 10;

        UserSortField resolvedSortField = sortField != null ? sortField : UserSortField.ID;

        SortDirection resolvedDirection = direction != null ? direction : SortDirection.ASC;

        return userRepositoryPort.search(
                filter,
                resolvedPage,
                resolvedSize,
                resolvedSortField,
                resolvedDirection);
    }
}
