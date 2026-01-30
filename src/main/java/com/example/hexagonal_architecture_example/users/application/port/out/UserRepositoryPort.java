package com.example.hexagonal_architecture_example.users.application.port.out;

import java.util.Optional;

import com.example.hexagonal_architecture_example.users.application.common.PageResult;
import com.example.hexagonal_architecture_example.users.application.common.SortDirection;
import com.example.hexagonal_architecture_example.users.application.common.UserSearchFilter;
import com.example.hexagonal_architecture_example.users.application.common.UserSortField;
import com.example.hexagonal_architecture_example.users.domain.model.User;

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