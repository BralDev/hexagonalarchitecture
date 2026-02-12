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
        @Operation(
            summary = "Crear usuario",
            description = "Crea un nuevo usuario en el sistema con validación de documento. El documento debe cumplir con el formato del tipo seleccionado: DNI (8 dígitos), CE (1-20 caracteres numéricos), PASSPORT (6-20 alfanuméricos), TI (1-20 numéricos). La contraseña debe tener mínimo 8 caracteres. Usuario es creado con estado ACTIVE por defecto. Ejemplo JSON: {\"username\":\"juan.perez\",\"firstName\":\"Juan\",\"lastName\":\"Perez\",\"email\":\"juan@example.com\",\"phone\":\"3101234567\",\"documentType\":\"DNI\",\"documentNumber\":\"12345678\",\"address\":\"Calle 123 #45\",\"birthDate\":\"1990-05-15\",\"password\":\"Secure123!@\"}"
        )
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente. Retorna el usuario con estado ACTIVE")
        @ApiResponse(responseCode = "400", description = "Error de validación: valores inválidos, documento mal formateado, edad menor a 18 años")
        @ApiResponse(responseCode = "409", description = "Conflicto: username, email o documento ya existe en el sistema")
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
        @Operation(
            summary = "Obtener usuario por ID",
            description = "Recupera todos los datos de un usuario específico. El ID es un UUID generado automáticamente al crear el usuario. Ejemplo: GET /users/550e8400-e29b-41d4-a716-446655440000"
        )
        @ApiResponse(responseCode = "200", description = "Usuario encontrado. Retorna objeto completo del usuario")
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado con el ID proporcionado")
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
        @Operation(
            summary = "Actualizar usuario",
            description = "Actualiza los datos de un usuario existente. IMPORTANTE: El tipo y número de documento NO pueden ser modificados. Solo se pueden actualizar: username, firstName, lastName, email, phone, address, birthDate, status. Ejemplo: PUT /users/550e8400-e29b-41d4-a716-446655440000 con JSON: {\"username\":\"juan.p\",\"firstName\":\"Juan\",\"lastName\":\"Perez Updated\",\"email\":\"juan.new@example.com\",\"phone\":\"3109999999\",\"address\":\"Calle 999 #99\",\"birthDate\":\"1990-05-15\",\"status\":\"ACTIVE\"}"
        )
        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente")
        @ApiResponse(responseCode = "400", description = "Datos inválidos o validación fallida")
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        @ApiResponse(responseCode = "409", description = "Conflicto: username o email ya existe en otro usuario")
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
        @Operation(
            summary = "Eliminar usuario",
            description = "Realiza eliminación LÓGICA del usuario (soft delete). El usuario NO se elimina de la base de datos, solo cambia su estado a DELETED. El usuario no aparecerá en búsquedas por estado ACTIVE. IMPORTANTE: No se puede eliminar al usuario administrador del sistema. Ejemplo: DELETE /users/550e8400-e29b-41d4-a716-446655440000"
        )
        @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente (sin contenido en respuesta)")
        @ApiResponse(responseCode = "400", description = "No se puede eliminar al usuario administrador")
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        public void deleteById(@PathVariable String id) {
                deleteUserUseCase.execute(id);
        }

        @PostMapping("/{id}/activate")
        @Operation(
            summary = "Activar usuario",
            description = "Cambia el estado del usuario a ACTIVE. El usuario podrá ser encontrado en búsquedas. Ejemplo: POST /users/550e8400-e29b-41d4-a716-446655440000/activate"
        )
        @ApiResponse(responseCode = "200", description = "Usuario activado exitosamente. Retorna el usuario con estado ACTIVE")
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
        @Operation(
            summary = "Desactivar usuario",
            description = "Cambia el estado del usuario a INACTIVE. El usuario no aparecerá en búsquedas por estado ACTIVE. Ejemplo: POST /users/550e8400-e29b-41d4-a716-446655440000/deactivate"
        )
        @ApiResponse(responseCode = "200", description = "Usuario desactivado exitosamente. Retorna el usuario con estado INACTIVE")
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
        @Operation(
            summary = "Cambiar contraseña",
            description = "Cambia la contraseña del usuario. Requiere proporcionar la contraseña actual para validación. Las contraseñas nuevas deben coinciden exactamente. Mínimo 8 caracteres. Ejemplo: POST /users/550e8400-e29b-41d4-a716-446655440000/password con JSON: {\"currentPassword\":\"OldPass123!@\",\"newPassword\":\"NewPass456!@\",\"confirmPassword\":\"NewPass456!@\"}"
        )
        @ApiResponse(responseCode = "204", description = "Contraseña cambiada exitosamente (sin contenido en respuesta)")
        @ApiResponse(responseCode = "400", description = "Contraseña actual incorrecta o nuevas contraseñas no coinciden")
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        public void changePassword(@PathVariable String id, @Valid @RequestBody ChangePasswordRequest request) {
                changePasswordUseCase.execute(
                                id,
                                request.currentPassword(),
                                request.newPassword(),
                                request.confirmPassword());
        }

        @GetMapping("/search/lastName")
        @Operation(
            summary = "Buscar usuarios por apellido",
            description = "Busca usuarios filtrando por apellido (búsqueda parcial, case-insensitive) con paginación. Parámetros: lastName (String, requerido), status (UserStatus, default: ACTIVE), page (int, default: 0), size (int, default: 10, máximo: 100), sortField (UserSortField: ID/LAST_NAME/DOCUMENT_NUMBER, default: LAST_NAME), direction (SortDirection: ASC/DESC, default: ASC). Ejemplo: GET /users/search/lastName?lastName=Garcia&status=ACTIVE&page=0&size=10&sortField=LAST_NAME&direction=ASC"
        )
        @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente. Retorna lista paginada de usuarios")
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
        @Operation(
            summary = "Buscar usuarios por número de documento",
            description = "Busca usuarios filtrando por número de documento (búsqueda parcial, case-insensitive) con paginación. Parámetros: documentNumber (String, requerido), status (UserStatus, default: ACTIVE), page (int, default: 0), size (int, default: 10, máximo: 100), sortField (UserSortField: ID/LAST_NAME/DOCUMENT_NUMBER, default: DOCUMENT_NUMBER), direction (SortDirection: ASC/DESC, default: ASC). Ejemplo: GET /users/search/documentNumber?documentNumber=12345&status=ACTIVE&page=0&size=10"
        )
        @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente. Retorna lista paginada de usuarios")
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
        @Operation(
            summary = "Búsqueda avanzada de usuarios",
            description = "Búsqueda con múltiples criterios opcionales: apellido, número de documento, estado, rango de fecha de nacimiento. Parámetros: lastname (String, opcional), documentNumber (String, opcional), status (UserStatus, default: ACTIVE), birthDateFrom (LocalDate, formato: yyyy-MM-dd, opcional), birthDateTo (LocalDate, formato: yyyy-MM-dd, opcional), page (int, default: 0), size (int, default: 10, máximo: 100), sortField (UserSortField: ID/LAST_NAME/DOCUMENT_NUMBER, default: ID), direction (SortDirection: ASC/DESC, default: ASC). Ejemplo: GET /users?lastname=Garcia&documentNumber=123&status=ACTIVE&birthDateFrom=1990-01-01&birthDateTo=2000-12-31&page=0&size=10"
        )
        @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente. Retorna lista paginada de usuarios")
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