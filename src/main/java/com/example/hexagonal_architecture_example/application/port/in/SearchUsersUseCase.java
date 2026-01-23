package com.example.hexagonal_architecture_example.application.port.in;

import com.example.hexagonal_architecture_example.application.common.PageResult;
import com.example.hexagonal_architecture_example.application.common.SortDirection;
import com.example.hexagonal_architecture_example.application.common.UserSortField;
import com.example.hexagonal_architecture_example.application.port.out.UserRepositoryPort;
import com.example.hexagonal_architecture_example.domain.model.User;

public class SearchUsersUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public SearchUsersUseCase(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    public PageResult<User> execute(
            String firstName,
            String lastName,
            int page,
            int size,
            UserSortField sortField,
            SortDirection direction
    ) {
        return userRepositoryPort.search(
                firstName,
                lastName,
                page,
                size,
                sortField,
                direction
        );
    }
}

