package com.example.hexagonalarchitecture.users.infraestructure.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.hexagonalarchitecture.users.application.common.PageResult;
import com.example.hexagonalarchitecture.users.application.common.SortDirection;
import com.example.hexagonalarchitecture.users.application.common.UserSearchFilter;
import com.example.hexagonalarchitecture.users.application.common.UserSortField;
import com.example.hexagonalarchitecture.users.application.port.in.ActivateUserUseCase;
import com.example.hexagonalarchitecture.users.application.port.in.ChangePasswordUseCase;
import com.example.hexagonalarchitecture.users.application.port.in.CreateUserUseCase;
import com.example.hexagonalarchitecture.users.application.port.in.DeactivateUserUseCase;
import com.example.hexagonalarchitecture.users.application.port.in.DeleteUserUseCase;
import com.example.hexagonalarchitecture.users.application.port.in.GetUserByIdUseCase;
import com.example.hexagonalarchitecture.users.application.port.in.GetUsersByFirstNameUseCase;
import com.example.hexagonalarchitecture.users.application.port.in.GetUsersByLastNameUseCase;
import com.example.hexagonalarchitecture.users.application.port.in.SearchUsersUseCase;
import com.example.hexagonalarchitecture.users.application.port.in.UpdateUserUseCase;
import com.example.hexagonalarchitecture.users.domain.model.User;
import com.example.hexagonalarchitecture.users.domain.model.UserStatus;
import com.example.hexagonalarchitecture.users.infraestructure.controller.dto.CreateUserRequest;
import com.example.hexagonalarchitecture.users.infraestructure.controller.dto.ChangePasswordRequest;
import com.example.hexagonalarchitecture.users.infraestructure.controller.dto.PageMeta;
import com.example.hexagonalarchitecture.users.infraestructure.controller.dto.PageResponse;
import com.example.hexagonalarchitecture.users.infraestructure.controller.dto.UpdateUserRequest;
import com.example.hexagonalarchitecture.users.infraestructure.controller.dto.UserResponse;

import java.net.URI;
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

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Users", description = "Operaciones relacionadas con usuarios")
@RestController
@RequestMapping("/users")
public class UserController {

        private final CreateUserUseCase createUserUseCase;
        private final UpdateUserUseCase updateUserUseCase;
        private final DeleteUserUseCase deleteUserUseCase;
	private final ActivateUserUseCase activateUserUseCase;
	private final DeactivateUserUseCase deactivateUserUseCase;
        private final ChangePasswordUseCase changePasswordUseCase;
	private final GetUserByIdUseCase getUserUseCase;
	private final GetUsersByFirstNameUseCase getUsersByFirstNameUseCase;
	private final GetUsersByLastNameUseCase getUsersByLastNameUseCase;
	private final SearchUsersUseCase searchUsersUseCase;

	public UserController(
			CreateUserUseCase createUserUseCase,
			UpdateUserUseCase updateUserUseCase,
			DeleteUserUseCase deleteUserUseCase,
			ActivateUserUseCase activateUserUseCase,
			DeactivateUserUseCase deactivateUserUseCase,
                        ChangePasswordUseCase changePasswordUseCase,
			GetUserByIdUseCase getUserUseCase,
			GetUsersByFirstNameUseCase getUsersByFirstNameUseCase,
			GetUsersByLastNameUseCase getUsersByLastNameUseCase,
			SearchUsersUseCase searchUsersUseCase) {
		this.createUserUseCase = createUserUseCase;
		this.updateUserUseCase = updateUserUseCase;
		this.deleteUserUseCase = deleteUserUseCase;
		this.activateUserUseCase = activateUserUseCase;
		this.deactivateUserUseCase = deactivateUserUseCase;
                this.changePasswordUseCase = changePasswordUseCase;
                this.getUserUseCase = getUserUseCase;
                this.getUsersByFirstNameUseCase = getUsersByFirstNameUseCase;
                this.getUsersByLastNameUseCase = getUsersByLastNameUseCase;
                this.searchUsersUseCase = searchUsersUseCase;
        }

        @PostMapping
	public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
		final User user = new User(
				null,
                                request.username(),
				request.firstName(),
				request.lastName(),
                                request.email(),
                                request.phone(),
                                request.document(),
                                request.address(),
				null,
				request.birthDate());
                final User userCreated = createUserUseCase.execute(user, request.password());

                UserResponse response = new UserResponse(
                                userCreated.id(),
                                userCreated.username(),
                                userCreated.firstName(),
                                userCreated.lastName(),
                                userCreated.email(),
                                userCreated.phone(),
                                userCreated.document(),
                                userCreated.address(),
                                userCreated.birthDate(),
                                userCreated.status());
                
                return ResponseEntity
                                .created(URI.create("/users/" + userCreated.id()))
                                .body(response);
        }

        @GetMapping("/{id}")
        public UserResponse getById(@PathVariable Long id) {
                final User user = getUserUseCase.execute(id);

                return new UserResponse(
                                user.id(),
                                user.username(),
                                user.firstName(),
                                user.lastName(),
                                user.email(),
                                user.phone(),
                                user.document(),
                                user.address(),
                                user.birthDate(),
                                user.status());
        }

        @PutMapping("/{id}")
	public UserResponse update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
		final User user = new User(
				null,
                                request.username(),
				request.firstName(),
				request.lastName(),
                                request.email(),
                                request.phone(),
                                request.document(),
                                request.address(),
				request.status(),
				request.birthDate());
                final User updatedUser = updateUserUseCase.execute(id, user);

                return new UserResponse(
                                updatedUser.id(),
                                updatedUser.username(),
                                updatedUser.firstName(),
                                updatedUser.lastName(),
                                updatedUser.email(),
                                updatedUser.phone(),
                                updatedUser.document(),
                                updatedUser.address(),
                                updatedUser.birthDate(),
                                updatedUser.status());
        }

        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void deleteById(@PathVariable Long id) {
                deleteUserUseCase.execute(id);
        }

        @PostMapping("/{id}/activate")
        public UserResponse activateById(@PathVariable Long id) {
                final User activatedUser = activateUserUseCase.execute(id);

                return new UserResponse(
                                activatedUser.id(),
                                activatedUser.username(),
                                activatedUser.firstName(),
                                activatedUser.lastName(),
                                activatedUser.email(),
                                activatedUser.phone(),
                                activatedUser.document(),
                                activatedUser.address(),
                                activatedUser.birthDate(),
                                activatedUser.status());
        }

        @PostMapping("/{id}/deactivate")
        public UserResponse deactivateById(@PathVariable Long id) {
                final User deactivatedUser = deactivateUserUseCase.execute(id);

                return new UserResponse(
                                deactivatedUser.id(),
                                deactivatedUser.username(),
                                deactivatedUser.firstName(),
                                deactivatedUser.lastName(),
                                deactivatedUser.email(),
                                deactivatedUser.phone(),
                                deactivatedUser.document(),
                                deactivatedUser.address(),
                                deactivatedUser.birthDate(),
                                deactivatedUser.status());
        }

        @PostMapping("/{id}/password")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void changePassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordRequest request) {
                changePasswordUseCase.execute(
                                id,
                                request.currentPassword(),
                                request.newPassword(),
                                request.confirmPassword());
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
                                                                u.username(),
                                                                u.firstName(),
                                                                u.lastName(),
                                                                u.email(),
                                                                u.phone(),
                                                                u.document(),
                                                                u.address(),
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
                                                                u.username(),
                                                                u.firstName(),
                                                                u.lastName(),
                                                                u.email(),
                                                                u.phone(),
                                                                u.document(),
                                                                u.address(),
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
                                                                u.username(),
                                                                u.firstName(),
                                                                u.lastName(),
                                                                u.email(),
                                                                u.phone(),
                                                                u.document(),
                                                                u.address(),
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