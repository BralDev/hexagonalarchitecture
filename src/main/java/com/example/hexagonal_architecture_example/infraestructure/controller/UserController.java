package com.example.hexagonal_architecture_example.infraestructure.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.hexagonal_architecture_example.application.port.in.CreateUserUseCase;
import com.example.hexagonal_architecture_example.application.port.in.GetUserByIdUseCase;
import com.example.hexagonal_architecture_example.application.port.in.GetUsersByFirstNameUseCase;
import com.example.hexagonal_architecture_example.application.port.in.GetUsersByLastNameUseCase;
import com.example.hexagonal_architecture_example.domain.model.User;
import com.example.hexagonal_architecture_example.infraestructure.controller.dto.UserReponse;
import com.example.hexagonal_architecture_example.infraestructure.controller.dto.UserRequest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetUserByIdUseCase getUserUseCase;
    private final GetUsersByFirstNameUseCase getUsersByFirstNameUseCase;
    private final GetUsersByLastNameUseCase getUsersByLastNameUseCase;

    public UserController(
            CreateUserUseCase createUserUseCase,
            GetUserByIdUseCase getUserUseCase,
            GetUsersByFirstNameUseCase getUsersByFirstNameUseCase,
            GetUsersByLastNameUseCase getUsersByLastNameUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.getUserUseCase = getUserUseCase;
        this.getUsersByFirstNameUseCase = getUsersByFirstNameUseCase;
        this.getUsersByLastNameUseCase = getUsersByLastNameUseCase;
    }

    @PostMapping
    public UserReponse createUser(@RequestBody UserRequest userRequest) {
        final User user = new User(
                null,
                userRequest.firstName(),
                userRequest.lastName());

        final User userCreated = createUserUseCase.execute(user);

        return new UserReponse(
                userCreated.id(),
                userCreated.firstName(),
                userCreated.lastName());
    }

    @GetMapping("/{id}")
    public UserReponse getUserById(@PathVariable Long id) {
        final User user = getUserUseCase.execute(id);

        return new UserReponse(
                user.id(),
                user.firstName(),
                user.lastName());
    }

    @GetMapping("/search/firstname/{firstName}")
    public List<UserReponse> getByFirstName(@PathVariable String firstName) {
        return getUsersByFirstNameUseCase.execute(firstName)
                .stream()
                .map(users -> new UserReponse(users.id(), users.firstName(), users.lastName()))
                .toList();
    }

    @GetMapping("/search/lastname/{lastName}")
    public List<UserReponse> getByLastName(@PathVariable String lastName) {
        return getUsersByLastNameUseCase.execute(lastName)
                .stream()
                .map(users -> new UserReponse(users.id(), users.firstName(), users.lastName()))
                .toList();
    }
}