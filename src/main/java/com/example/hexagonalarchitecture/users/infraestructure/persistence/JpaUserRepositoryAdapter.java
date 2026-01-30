package com.example.hexagonalarchitecture.users.infraestructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.example.hexagonalarchitecture.users.application.common.PageResult;
import com.example.hexagonalarchitecture.users.application.common.SortDirection;
import com.example.hexagonalarchitecture.users.application.common.UserSearchFilter;
import com.example.hexagonalarchitecture.users.application.common.UserSortField;
import com.example.hexagonalarchitecture.users.application.port.out.UserRepositoryPort;
import com.example.hexagonalarchitecture.users.domain.model.User;

@Repository
public class JpaUserRepositoryAdapter implements UserRepositoryPort {

        private final SpringDataUserRepository springDataUserRepository;

        public JpaUserRepositoryAdapter(SpringDataUserRepository springDataUserRepository) {
                this.springDataUserRepository = springDataUserRepository;
        }

        @Override
        public User save(User user) {
                UserEntity userEntity = new UserEntity(
                                user.id(),
                                user.firstName(),
                                user.lastName(),
                                user.status(),
                                user.birthDate());
                final UserEntity savedUser = springDataUserRepository.save(userEntity);
                return new User(
                                savedUser.id(),
                                savedUser.firstName(),
                                savedUser.lastName(),
                                savedUser.status(),
                                savedUser.birthDate());
        }

        @Override
        public Optional<User> findById(Long id) {
                final UserEntity savedUser = springDataUserRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("User not found"));
                return Optional.of(new User(
                                savedUser.id(),
                                savedUser.firstName(),
                                savedUser.lastName(),
                                savedUser.status(),
                                savedUser.birthDate()));
        }

        @Override
        public PageResult<User> search(
                        UserSearchFilter filter,
                        int page,
                        int size,
                        UserSortField sortField,
                        SortDirection direction) {

                Sort sort = Sort.by(
                                direction == SortDirection.ASC
                                                ? Sort.Direction.ASC
                                                : Sort.Direction.DESC,
                                sortField.column());

                Pageable pageable = PageRequest.of(page, size, sort);

                Specification<UserEntity> spec = Specification
                                .where(UserSpecifications.firstNameContains(filter.firstName()))
                                .and(UserSpecifications.lastNameContains(filter.lastName())
                                                .and(UserSpecifications.hasStatus(filter.status()))
                                                .and(UserSpecifications.birthDateFrom(filter.birthDateFrom()))
                                                .and(UserSpecifications.birthDateTo(filter.birthDateTo())));

                Page<UserEntity> result = springDataUserRepository.findAll(spec, pageable);

                List<User> users = result.getContent().stream()
                                .map(e -> new User(
                                                e.id(),
                                                e.firstName(),
                                                e.lastName(),
                                                e.status(),
                                                e.birthDate()))
                                .toList();

                return new PageResult<>(
                                users,
                                result.getNumber(),
                                result.getSize(),
                                result.getTotalElements(),
                                result.getTotalPages());
        }
}