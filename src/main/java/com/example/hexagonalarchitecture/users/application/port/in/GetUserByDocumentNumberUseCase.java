package com.example.hexagonalarchitecture.users.application.port.in;

import com.example.hexagonalarchitecture.users.application.common.PageResult;
import com.example.hexagonalarchitecture.users.application.common.SortDirection;
import com.example.hexagonalarchitecture.users.application.common.UserSearchFilter;
import com.example.hexagonalarchitecture.users.application.common.UserSortField;
import com.example.hexagonalarchitecture.users.application.port.out.UserRepositoryPort;
import com.example.hexagonalarchitecture.users.domain.model.User;
import com.example.hexagonalarchitecture.users.domain.model.UserStatus;

/**
 * Caso de uso para buscar usuarios por número de documento con filtros y paginación.
 * <p>
 * Aplica valores por defecto:
 * - status: ACTIVE si no se especifica
 * - page: 0 si es negativo
 * - size: 10 (min: 1, max: 100)
 * - sortField: DOCUMENT_NUMBER si no se especifica
 * - direction: ASC si no se especifica
 */
public class GetUserByDocumentNumberUseCase {

    private final UserRepositoryPort userRepository;

    public GetUserByDocumentNumberUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Busca usuarios por número de documento con paginación y filtros opcionales.
     * 
     * @param documentNumber número de documento a buscar (búsqueda parcial con LIKE)
     * @param status filtro por estado (default: ACTIVE)
     * @param page número de página (default: 0)
     * @param size tamaño de página (default: 10, max: 100)
     * @param sortField campo de ordenamiento (default: DOCUMENT_NUMBER)
     * @param direction dirección de ordenamiento (default: ASC)
     * @return resultado paginado de usuarios
     */
    public PageResult<User> execute(
            String documentNumber,
            UserStatus status,
            int page,
            int size,
            UserSortField sortField,
            SortDirection direction) {

        UserStatus resolvedStatus = status != null ? status : UserStatus.ACTIVE;

        UserSearchFilter filter = new UserSearchFilter(null, documentNumber, resolvedStatus, null, null);
        
        int resolvedPage = Math.max(page, 0);

        int resolvedSize = size > 0 ? Math.min(size, 100) : 10;

        UserSortField resolvedSortField = sortField != null ? sortField : UserSortField.DOCUMENT_NUMBER;

        SortDirection resolvedDirection = direction != null ? direction : SortDirection.ASC;

        return userRepository.search(
                filter,
                resolvedPage,
                resolvedSize,
                resolvedSortField,
                resolvedDirection);
    }
}
