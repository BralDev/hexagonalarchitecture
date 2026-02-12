package com.example.hexagonalarchitecture.users.application.port.out;

import java.util.Optional;

import com.example.hexagonalarchitecture.users.application.common.PageResult;
import com.example.hexagonalarchitecture.users.application.common.SortDirection;
import com.example.hexagonalarchitecture.users.application.common.UserSearchFilter;
import com.example.hexagonalarchitecture.users.application.common.UserSortField;
import com.example.hexagonalarchitecture.users.domain.model.User;

public interface UserRepositoryPort {
    User create(User user, String hashedPassword);
    User update(User user, String passwordHash);
    Optional<User> findById(String id);
    UserWithPassword findByIdWithPassword(String id);
    PageResult<User> search(
        UserSearchFilter filter,
        int page,
        int size,
        UserSortField sortField,
        SortDirection direction);
    
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByDocumentNumber(String documentNumber);
}