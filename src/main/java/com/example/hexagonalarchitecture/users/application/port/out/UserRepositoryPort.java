package com.example.hexagonalarchitecture.users.application.port.out;

import java.util.Optional;

import com.example.hexagonalarchitecture.users.application.common.PageResult;
import com.example.hexagonalarchitecture.users.application.common.SortDirection;
import com.example.hexagonalarchitecture.users.application.common.UserSearchFilter;
import com.example.hexagonalarchitecture.users.application.common.UserSortField;
import com.example.hexagonalarchitecture.users.domain.model.User;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findById(Long id);
    PageResult<User> search(
        UserSearchFilter filter,
        int page,
        int size,
        UserSortField sortField,
        SortDirection direction);
}