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
import com.example.hexagonalarchitecture.users.application.port.out.UserWithPassword;
import com.example.hexagonalarchitecture.users.domain.model.User;

@Repository
public class JpaUserRepositoryAdapter implements UserRepositoryPort {

        private final SpringDataUserRepository springDataUserRepository;

        public JpaUserRepositoryAdapter(SpringDataUserRepository springDataUserRepository) {
                this.springDataUserRepository = springDataUserRepository;
        }

        @Override
        public User create(User user, String hashedPassword) {
                return saveUser(user, hashedPassword);
        }

        @Override
        public User update(User user, String passwordHash) {
                return saveUser(user, passwordHash);
        }

        @Override
        public Optional<User> findById(Long id) {
                final UserEntity userEntity = springDataUserRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("User not found"));
                return Optional.of(userEntityToDomain(userEntity));
        }

        @Override
        public UserWithPassword findByIdWithPassword(Long id) {
                final UserEntity userEntity = springDataUserRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("User not found"));
                return new UserWithPassword(
                                userEntityToDomain(userEntity),
                                userEntity.getPassword());
        }

        private User userEntityToDomain(UserEntity entity) {
                return new User(
                                entity.getId(),
                                entity.getUsername(),
                                entity.getFirstName(),
                                entity.getLastName(),
                                entity.getEmail(),
                                entity.getPhone(),
                                entity.getDocument(),
                                entity.getAddress(),
                                entity.getStatus(),
                                entity.getBirthDate());
        }

        private User saveUser(User user, String passwordHash) {
                UserEntity userEntity = new UserEntity(
                                user.id(),
                                user.username(),
                                passwordHash,
                                user.firstName(),
                                user.lastName(),
                                user.email(),
                                user.phone(),
                                user.document(),
                                user.address(),
                                user.status(),
                                user.birthDate());
                final UserEntity savedUser = springDataUserRepository.save(userEntity);
                return userEntityToDomain(savedUser);
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
                                .map(this::userEntityToDomain)
                                .toList();

                return new PageResult<>(
                                users,
                                result.getNumber(),
                                result.getSize(),
                                result.getTotalElements(),
                                result.getTotalPages());
        }
}