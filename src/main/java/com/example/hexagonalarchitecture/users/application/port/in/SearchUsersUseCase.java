package com.example.hexagonalarchitecture.users.application.port.in;

import com.example.hexagonalarchitecture.users.application.common.PageResult;
import com.example.hexagonalarchitecture.users.application.common.SortDirection;
import com.example.hexagonalarchitecture.users.application.common.UserSearchFilter;
import com.example.hexagonalarchitecture.users.application.common.UserSortField;
import com.example.hexagonalarchitecture.users.application.port.out.UserRepositoryPort;
import com.example.hexagonalarchitecture.users.domain.model.User;

public class SearchUsersUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public SearchUsersUseCase(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

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
