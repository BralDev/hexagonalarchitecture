package com.example.hexagonal_architecture_example.application.port.out;

import java.util.Optional;

import com.example.hexagonal_architecture_example.application.common.PageResult;
import com.example.hexagonal_architecture_example.domain.model.User;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findById(Long id);
    PageResult<User> findByFirstNameContaining(String firstName, int page, int size);
    PageResult<User> findByLastNameContaining(String lastName, int page, int size);
    PageResult<User> search(String firstName, String lastName, int page, int size);
}