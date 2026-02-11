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
import com.example.hexagonalarchitecture.users.application.port.in.GetUserByDocumentNumberUseCase;
import com.example.hexagonalarchitecture.users.application.port.in.GetUserByIdUseCase;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

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
	private final GetUsersByLastNameUseCase getUsersByLastNameUseCase;
	private final GetUserByDocumentNumberUseCase getUserByDocumentNumberUseCase;
	private final SearchUsersUseCase searchUsersUseCase;

	public UserController(
			CreateUserUseCase createUserUseCase,
			UpdateUserUseCase updateUserUseCase,
			DeleteUserUseCase deleteUserUseCase,
			ActivateUserUseCase activateUserUseCase,
			DeactivateUserUseCase deactivateUserUseCase,
                        ChangePasswordUseCase changePasswordUseCase,
			GetUserByIdUseCase getUserUseCase,			
			GetUsersByLastNameUseCase getUsersByLastNameUseCase,
                        GetUserByDocumentNumberUseCase getUserByDocumentNumberUseCase,
			SearchUsersUseCase searchUsersUseCase) {
		this.createUserUseCase = createUserUseCase;
		this.updateUserUseCase = updateUserUseCase;
		this.deleteUserUseCase = deleteUserUseCase;
		this.activateUserUseCase = activateUserUseCase;
		this.deactivateUserUseCase = deactivateUserUseCase;
                this.changePasswordUseCase = changePasswordUseCase;
                this.getUserUseCase = getUserUseCase;                
                this.getUsersByLastNameUseCase = getUsersByLastNameUseCase;
                this.getUserByDocumentNumberUseCase = getUserByDocumentNumberUseCase;
                this.searchUsersUseCase = searchUsersUseCase;
        }

        @PostMapping
        @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario en el sistema con validación de documento")
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente")
        @ApiResponse(responseCode = "400", description = "Datos inválidos o documento con formato incorrecto")
	public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
		final User user = new User(
				null,
                                request.username(),
				request.firstName(),
				request.lastName(),
                                request.email(),
                                request.phone(),
                                request.documentType(),
                                request.documentNumber(),
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
                                userCreated.documentType(),
                                userCreated.documentNumber(),
                                userCreated.address(),
                                userCreated.birthDate(),
                                userCreated.status());
                
                return ResponseEntity
                                .created(URI.create("/users/" + userCreated.id()))
                                .body(response);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Obtener usuario por ID", description = "Recupera los datos de un usuario específico por su identificador")
        @ApiResponse(responseCode = "200", description = "Usuario encontrado")
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        public UserResponse getById(@PathVariable String id) {
                final User user = getUserUseCase.execute(id);

                return new UserResponse(
                                user.id(),
                                user.username(),
                                user.firstName(),
                                user.lastName(),
                                user.email(),
                                user.phone(),
                                user.documentType(),
                                user.documentNumber(),
                                user.address(),
                                user.birthDate(),
                                user.status());
        }

        @PutMapping("/{id}")
        @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente (excepto documento)")
        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente")
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
	public UserResponse update(@PathVariable String id, @Valid @RequestBody UpdateUserRequest request) {
		final User existingUser = getUserUseCase.execute(id);
			
		final User user = new User(
				null,
                                request.username(),
				request.firstName(),
				request.lastName(),
                                request.email(),
                                request.phone(),
                                existingUser.documentType(),    
                                existingUser.documentNumber(),
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
                                updatedUser.documentType(),
                                updatedUser.documentNumber(),
                                updatedUser.address(),
                                updatedUser.birthDate(),
                                updatedUser.status());
        }

        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        @Operation(summary = "Eliminar usuario", description = "Realiza eliminación lógica del usuario (cambia estado a DELETED)")
        @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente")
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        public void deleteById(@PathVariable String id) {
                deleteUserUseCase.execute(id);
        }

        @PostMapping("/{id}/activate")
        @Operation(summary = "Activar usuario", description = "Cambia el estado del usuario a ACTIVE")
        @ApiResponse(responseCode = "200", description = "Usuario activado exitosamente")
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        public UserResponse activateById(@PathVariable String id) {
                final User activatedUser = activateUserUseCase.execute(id);

                return new UserResponse(
                                activatedUser.id(),
                                activatedUser.username(),
                                activatedUser.firstName(),
                                activatedUser.lastName(),
                                activatedUser.email(),
                                activatedUser.phone(),
                                activatedUser.documentType(),
                                activatedUser.documentNumber(),
                                activatedUser.address(),
                                activatedUser.birthDate(),
                                activatedUser.status());
        }

        @PostMapping("/{id}/deactivate")
        @Operation(summary = "Desactivar usuario", description = "Cambia el estado del usuario a INACTIVE")
        @ApiResponse(responseCode = "200", description = "Usuario desactivado exitosamente")
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        public UserResponse deactivateById(@PathVariable String id) {
                final User deactivatedUser = deactivateUserUseCase.execute(id);

                return new UserResponse(
                                deactivatedUser.id(),
                                deactivatedUser.username(),
                                deactivatedUser.firstName(),
                                deactivatedUser.lastName(),
                                deactivatedUser.email(),
                                deactivatedUser.phone(),
                                deactivatedUser.documentType(),
                                deactivatedUser.documentNumber(),
                                deactivatedUser.address(),
                                deactivatedUser.birthDate(),
                                deactivatedUser.status());
        }

        @PostMapping("/{id}/password")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        @Operation(summary = "Cambiar contraseña", description = "Cambia la contraseña del usuario verificando la contraseña actual")
        @ApiResponse(responseCode = "204", description = "Contraseña cambiada exitosamente")
        @ApiResponse(responseCode = "400", description = "Contraseña actual incorrecta o confirmación no coincide")
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        public void changePassword(@PathVariable String id, @Valid @RequestBody ChangePasswordRequest request) {
                changePasswordUseCase.execute(
                                id,
                                request.currentPassword(),
                                request.newPassword(),
                                request.confirmPassword());
        }

        @GetMapping("/search/lastName")
        @Operation(summary = "Buscar usuarios por apellido", description = "Busca usuarios filtrando por apellido con paginación")
        @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente")
        public PageResponse<UserResponse> searchByLastName(
                        @RequestParam String lastName,
                        @RequestParam(required = false, defaultValue = "ACTIVE") UserStatus status,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(required = false, defaultValue = "LAST_NAME") UserSortField sortField,
                        @RequestParam(required = false, defaultValue = "ASC") SortDirection direction) {

                PageResult<User> result = getUsersByLastNameUseCase.execute(
                                lastName,
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
                                                                u.documentType(),
                                                                u.documentNumber(),
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

        @GetMapping("/search/documentNumber")
        @Operation(summary = "Buscar usuarios por documento", description = "Busca usuarios filtrando por número de documento con paginación")
        @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente")
        public PageResponse<UserResponse> searchByDocumentNumber(
                        @RequestParam String documentNumber,
                        @RequestParam(required = false, defaultValue = "ACTIVE") UserStatus status,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(required = false, defaultValue = "DOCUMENT_NUMBER") UserSortField sortField,
                        @RequestParam(required = false, defaultValue = "ASC") SortDirection direction) {

                PageResult<User> result = getUserByDocumentNumberUseCase.execute(
                                documentNumber,
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
                                                                u.documentType(),
                                                                u.documentNumber(),
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
        @Operation(summary = "Buscar usuarios (búsqueda avanzada)", description = "Búsqueda avanzada de usuarios por múltiples criterios: apellido, documento, estado, rango de fecha de nacimiento con paginación")
        @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente")
        public PageResponse<UserResponse> search(                        
                        @RequestParam(required = false) String lastName,
                        @RequestParam(required = false) String documentNumber,
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
                                lastName,
                                documentNumber,
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
                                                                u.documentType(),
                                                                u.documentNumber(),
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