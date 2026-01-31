package com.example.hexagonalarchitecture.users.infraestructure.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.hexagonalarchitecture.users.application.common.PageResult;
import com.example.hexagonalarchitecture.users.application.common.SortDirection;
import com.example.hexagonalarchitecture.users.application.common.UserSearchFilter;
import com.example.hexagonalarchitecture.users.application.common.UserSortField;
import com.example.hexagonalarchitecture.users.application.port.in.CreateUserUseCase;
import com.example.hexagonalarchitecture.users.application.port.in.DeleteUserUseCase;
import com.example.hexagonalarchitecture.users.application.port.in.GetUserByIdUseCase;
import com.example.hexagonalarchitecture.users.application.port.in.GetUsersByFirstNameUseCase;
import com.example.hexagonalarchitecture.users.application.port.in.GetUsersByLastNameUseCase;
import com.example.hexagonalarchitecture.users.application.port.in.SearchUsersUseCase;
import com.example.hexagonalarchitecture.users.application.port.in.UpdateUserUseCase;
import com.example.hexagonalarchitecture.users.domain.model.User;
import com.example.hexagonalarchitecture.users.domain.model.UserStatus;
import com.example.hexagonalarchitecture.users.infraestructure.controller.dto.PageMeta;
import com.example.hexagonalarchitecture.users.infraestructure.controller.dto.PageResponse;
import com.example.hexagonalarchitecture.users.infraestructure.controller.dto.UserRequest;
import com.example.hexagonalarchitecture.users.infraestructure.controller.dto.UserResponse;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Users", description = "Operaciones relacionadas con usuarios")
@RestController
@RequestMapping("/users")
public class UserController {

        private final CreateUserUseCase createUserUseCase;
        private final UpdateUserUseCase updateUserUseCase;
        private final DeleteUserUseCase deleteUserUseCase;
        private final GetUserByIdUseCase getUserUseCase;
        private final GetUsersByFirstNameUseCase getUsersByFirstNameUseCase;
        private final GetUsersByLastNameUseCase getUsersByLastNameUseCase;
        private final SearchUsersUseCase searchUsersUseCase;

        public UserController(
                        CreateUserUseCase createUserUseCase,
                        UpdateUserUseCase updateUserUseCase,
                        DeleteUserUseCase deleteUserUseCase,
                        GetUserByIdUseCase getUserUseCase,
                        GetUsersByFirstNameUseCase getUsersByFirstNameUseCase,
                        GetUsersByLastNameUseCase getUsersByLastNameUseCase,
                        SearchUsersUseCase searchUsersUseCase) {
                this.createUserUseCase = createUserUseCase;
                this.updateUserUseCase = updateUserUseCase;
                this.deleteUserUseCase = deleteUserUseCase;
                this.getUserUseCase = getUserUseCase;
                this.getUsersByFirstNameUseCase = getUsersByFirstNameUseCase;
                this.getUsersByLastNameUseCase = getUsersByLastNameUseCase;
                this.searchUsersUseCase = searchUsersUseCase;
        }

        @PostMapping
        public UserResponse create(@RequestBody UserRequest userRequest) {
                final User user = new User(
                                null,
                                userRequest.firstName(),
                                userRequest.lastName(),
                                null,
                                userRequest.birthDate());

                final User userCreated = createUserUseCase.execute(user);

                return new UserResponse(
                                userCreated.id(),
                                userCreated.firstName(),
                                userCreated.lastName(),
                                userCreated.birthDate(),
                                userCreated.status());
        }

        @GetMapping("/{id}")
        public UserResponse getById(@PathVariable Long id) {
                final User user = getUserUseCase.execute(id);

                return new UserResponse(
                                user.id(),
                                user.firstName(),
                                user.lastName(),
                                user.birthDate(),
                                user.status());
        }

        @PutMapping("/{id}")
        public UserResponse update(@PathVariable Long id, @RequestBody UserRequest userRequest) {
                final User user = new User(
                                null,
                                userRequest.firstName(),
                                userRequest.lastName(),
                                userRequest.status(),
                                userRequest.birthDate());

                final User updatedUser = updateUserUseCase.execute(id, user);

                return new UserResponse(
                                updatedUser.id(),
                                updatedUser.firstName(),
                                updatedUser.lastName(),
                                updatedUser.birthDate(),
                                updatedUser.status());
        }

        @DeleteMapping("/{id}")
        public void deleteById(@PathVariable Long id) {
                deleteUserUseCase.execute(id);
        }

        @GetMapping("/search/firstname")
        public PageResponse<UserResponse> searchByFirstName(
                        @RequestParam String value,
                        @RequestParam(required = false, defaultValue = "ACTIVE") UserStatus status,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(required = false, defaultValue = "FIRST_NAME") UserSortField sortField,
                        @RequestParam(required = false, defaultValue = "ASC") SortDirection direction) {

                PageResult<User> result = getUsersByFirstNameUseCase.execute(
                                value,
                                status,
                                page,
                                size,
                                sortField,
                                direction);

                return new PageResponse<>(
                                result.content().stream()
                                                .map(u -> new UserResponse(
                                                                u.id(),
                                                                u.firstName(),
                                                                u.lastName(),
                                                                u.birthDate(),
                                                                u.status()))
                                                .toList(),
                                new PageMeta(
                                                result.page(),
                                                result.size(),
                                                result.totalElements(),
                                                result.totalPages()));
        }

        @GetMapping("/search/lastname")
        public PageResponse<UserResponse> searchByLastName(
                        @RequestParam String value,
                        @RequestParam(required = false, defaultValue = "ACTIVE") UserStatus status,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(required = false, defaultValue = "LAST_NAME") UserSortField sortField,
                        @RequestParam(required = false, defaultValue = "ASC") SortDirection direction) {

                PageResult<User> result = getUsersByLastNameUseCase.execute(
                                value,
                                status,
                                page,
                                size,
                                sortField,
                                direction);

                return new PageResponse<>(
                                result.content().stream()
                                                .map(u -> new UserResponse(
                                                                u.id(),
                                                                u.firstName(),
                                                                u.lastName(),
                                                                u.birthDate(),
                                                                u.status()))
                                                .toList(),
                                new PageMeta(
                                                result.page(),
                                                result.size(),
                                                result.totalElements(),
                                                result.totalPages()));
        }

        @GetMapping
        public PageResponse<UserResponse> search(
                        @RequestParam(required = false) String firstname,
                        @RequestParam(required = false) String lastname,
                        @RequestParam(required = false, defaultValue = "ACTIVE") UserStatus status,
                        @RequestParam(required = false)
                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                @Schema(
                                description = "Fecha de nacimiento desde, ejemplo: 1990-01-01",                                
                                format = "date"
                                )
                                LocalDate birthDateFrom,
                        @RequestParam(required = false)
                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                @Schema(
                                description = "Fecha de nacimiento hasta, ejemplo: 2005-12-20",                                
                                format = "date"
                                )
                                LocalDate birthDateTo,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(required = false, defaultValue = "ID") UserSortField sortField,
                        @RequestParam(required = false, defaultValue = "ASC") SortDirection direction) {

                UserSearchFilter filter = new UserSearchFilter(
                                firstname,
                                lastname,
                                status,
                                birthDateFrom,
                                birthDateTo);

                PageResult<User> result = searchUsersUseCase.execute(
                                filter,
                                page,
                                size,
                                sortField,
                                direction);

                return new PageResponse<>(
                                result.content().stream()
                                                .map(u -> new UserResponse(
                                                                u.id(),
                                                                u.firstName(),
                                                                u.lastName(),
                                                                u.birthDate(),
                                                                u.status()))
                                                .toList(),
                                new PageMeta(
                                                result.page(),
                                                result.size(),
                                                result.totalElements(),
                                                result.totalPages()));
        }
}