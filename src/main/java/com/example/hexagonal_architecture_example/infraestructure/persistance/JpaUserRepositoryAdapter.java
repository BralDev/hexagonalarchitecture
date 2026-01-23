package com.example.hexagonal_architecture_example.infraestructure.persistance;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.example.hexagonal_architecture_example.application.common.PageResult;
import com.example.hexagonal_architecture_example.application.common.SortDirection;
import com.example.hexagonal_architecture_example.application.common.UserSortField;
import com.example.hexagonal_architecture_example.application.port.out.UserRepositoryPort;
import com.example.hexagonal_architecture_example.domain.model.User;

@Repository
public class JpaUserRepositoryAdapter implements UserRepositoryPort {

        private final SpringDataUserRepository springDataUserRepository;

        public JpaUserRepositoryAdapter(SpringDataUserRepository springDataUserRepository) {
                this.springDataUserRepository = springDataUserRepository;
        }

        @Override
        public User save(User user) {
                UserEntity userEntity = new UserEntity(user.id(), user.firstName(), user.lastName());
                final UserEntity savedUser = springDataUserRepository.save(userEntity);
                return new User(savedUser.id(), savedUser.firstName(), savedUser.lastName());
        }

        @Override
        public Optional<User> findById(Long id) {
                final UserEntity savedUser = springDataUserRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("User not found"));
                return Optional.of(new User(savedUser.id(), savedUser.firstName(), savedUser.lastName()));
        }

        @Override
        public PageResult<User> findByFirstNameContaining(
                        String firstName,
                        int page,
                        int size) {
                Pageable pageable = PageRequest.of(page, size);

                Page<UserEntity> result = springDataUserRepository
                                .findByFirstNameContainingIgnoreCase(firstName, pageable);

                List<User> users = result.getContent().stream()
                                .map(e -> new User(e.id(), e.firstName(), e.lastName()))
                                .toList();

                return new PageResult<>(
                                users,
                                result.getNumber(),
                                result.getSize(),
                                result.getTotalElements(),
                                result.getTotalPages());
        }

        @Override
        public PageResult<User> findByLastNameContaining(
                        String lastName,
                        int page,
                        int size) {
                Pageable pageable = PageRequest.of(page, size);

                Page<UserEntity> result = springDataUserRepository
                                .findByLastNameContainingIgnoreCase(lastName, pageable);

                List<User> users = result.getContent().stream()
                                .map(e -> new User(e.id(), e.firstName(), e.lastName()))
                                .toList();

                return new PageResult<>(
                                users,
                                result.getNumber(),
                                result.getSize(),
                                result.getTotalElements(),
                                result.getTotalPages());
        }

        @Override
        public PageResult<User> search(
                        String firstName,
                        String lastName,
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
                                .where(UserSpecifications.firstNameContains(firstName))
                                .and(UserSpecifications.lastNameContains(lastName));

                Page<UserEntity> result = springDataUserRepository.findAll(spec, pageable);

                List<User> users = result.getContent().stream()
                                .map(e -> new User(e.id(), e.firstName(), e.lastName()))
                                .toList();

                return new PageResult<>(
                                users,
                                result.getNumber(),
                                result.getSize(),
                                result.getTotalElements(),
                                result.getTotalPages());
        }
}