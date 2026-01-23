package com.example.hexagonal_architecture_example.infraestructure.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.hexagonal_architecture_example.application.common.PageResult;
import com.example.hexagonal_architecture_example.application.port.in.CreateUserUseCase;
import com.example.hexagonal_architecture_example.application.port.in.GetUserByIdUseCase;
import com.example.hexagonal_architecture_example.application.port.in.GetUsersByFirstNameUseCase;
import com.example.hexagonal_architecture_example.application.port.in.GetUsersByLastNameUseCase;
import com.example.hexagonal_architecture_example.application.port.in.SearchUsersUseCase;
import com.example.hexagonal_architecture_example.domain.model.User;
import com.example.hexagonal_architecture_example.infraestructure.controller.dto.PageMeta;
import com.example.hexagonal_architecture_example.infraestructure.controller.dto.PageResponse;
import com.example.hexagonal_architecture_example.infraestructure.controller.dto.UserReponse;
import com.example.hexagonal_architecture_example.infraestructure.controller.dto.UserRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
    name = "Users",
    description = "Operaciones relacionadas con usuarios"
)
@RestController
@RequestMapping("/users")
public class UserController {

        private final CreateUserUseCase createUserUseCase;
        private final GetUserByIdUseCase getUserUseCase;
        private final GetUsersByFirstNameUseCase getUsersByFirstNameUseCase;
        private final GetUsersByLastNameUseCase getUsersByLastNameUseCase;
        private final SearchUsersUseCase searchUsersUseCase;

        public UserController(
                        CreateUserUseCase createUserUseCase,
                        GetUserByIdUseCase getUserUseCase,
                        GetUsersByFirstNameUseCase getUsersByFirstNameUseCase,
                        GetUsersByLastNameUseCase getUsersByLastNameUseCase,
                        SearchUsersUseCase searchUsersUseCase) {
                this.createUserUseCase = createUserUseCase;
                this.getUserUseCase = getUserUseCase;
                this.getUsersByFirstNameUseCase = getUsersByFirstNameUseCase;
                this.getUsersByLastNameUseCase = getUsersByLastNameUseCase;
                this.searchUsersUseCase = searchUsersUseCase;
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

        @GetMapping("/search/firstname")
        public PageResponse<UserReponse> searchByFirstName(
                        @RequestParam String value,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {

                PageResult<User> result = getUsersByFirstNameUseCase.execute(value, page, size);

                return new PageResponse<>(
                                result.content().stream()
                                                .map(u -> new UserReponse(u.id(), u.firstName(), u.lastName()))
                                                .toList(),
                                new PageMeta(
                                                result.page(),
                                                result.size(),
                                                result.totalElements(),
                                                result.totalPages()));
        }

        @GetMapping("/search/lastname")
        public PageResponse<UserReponse> searchByLastName(
                        @RequestParam String value,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {

                PageResult<User> result = getUsersByLastNameUseCase.execute(value, page, size);

                return new PageResponse<>(
                                result.content().stream()
                                                .map(u -> new UserReponse(u.id(), u.firstName(), u.lastName()))
                                                .toList(),
                                new PageMeta(
                                                result.page(),
                                                result.size(),
                                                result.totalElements(),
                                                result.totalPages()));
        }

        @GetMapping
        public PageResponse<UserReponse> searchUsers(
                        @RequestParam(required = false) String firstname,
                        @RequestParam(required = false) String lastname,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {

                PageResult<User> result = searchUsersUseCase.execute(firstname, lastname, page, size);

                return new PageResponse<>(
                                result.content().stream()
                                                .map(u -> new UserReponse(u.id(), u.firstName(), u.lastName()))
                                                .toList(),
                                new PageMeta(
                                                result.page(),
                                                result.size(),
                                                result.totalElements(),
                                                result.totalPages()));
        }
}