package com.example.hexagonal_architecture_example.application.port.out;

import java.util.List;
import java.util.Optional;

import com.example.hexagonal_architecture_example.domain.model.User;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findById(Long id);
    List<User> findByFirstName(String firstName);
    List<User> findByLastName(String lastName);
}
