package com.example.hexagonal_architecture_example.application.port.in;

import java.util.List;

import com.example.hexagonal_architecture_example.application.port.out.UserRepositoryPort;
import com.example.hexagonal_architecture_example.domain.model.User;

public class GetUsersByFirstNameUseCase {

    private final UserRepositoryPort userRepository;

    public GetUsersByFirstNameUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> execute(String firstName) {
        return userRepository.findByFirstName(firstName);
    }
}