package com.example.hexagonal_architecture_example.application.port.in;

import com.example.hexagonal_architecture_example.application.common.PageResult;
import com.example.hexagonal_architecture_example.application.common.SortDirection;
import com.example.hexagonal_architecture_example.application.common.UserSearchFilter;
import com.example.hexagonal_architecture_example.application.common.UserSortField;
import com.example.hexagonal_architecture_example.application.port.out.UserRepositoryPort;
import com.example.hexagonal_architecture_example.domain.model.User;
import com.example.hexagonal_architecture_example.domain.model.UserStatus;

public class GetUsersByLastNameUseCase {

    private final UserRepositoryPort userRepository;

    public GetUsersByLastNameUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    public PageResult<User> execute(
            String lastName,
            UserStatus status,
            int page,
            int size,
            UserSortField sortField,
            SortDirection direction) {

        UserStatus resolvedStatus = status != null ? status : UserStatus.ACTIVE;

        UserSearchFilter filter = new UserSearchFilter(null, lastName, resolvedStatus, null, null);
        
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