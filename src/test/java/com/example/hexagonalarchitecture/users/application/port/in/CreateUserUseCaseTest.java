package com.example.hexagonalarchitecture.users.application.port.in;

import com.example.hexagonalarchitecture.users.application.port.out.UserRepositoryPort;
import com.example.hexagonalarchitecture.users.domain.model.DocumentType;
import com.example.hexagonalarchitecture.users.domain.model.User;
import com.example.hexagonalarchitecture.users.domain.model.UserStatus;
import com.example.hexagonalarchitecture.users.infraestructure.exception.InvalidDocumentException;
import com.example.hexagonalarchitecture.users.infraestructure.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateUserUseCase - Tests de caso de uso")
class CreateUserUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;

    private PasswordEncoder passwordEncoder;
    private CreateUserUseCase createUserUseCase;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        createUserUseCase = new CreateUserUseCase(userRepository, passwordEncoder);
    }

    @Test
    @DisplayName("Crear usuario válido con DNI correcto")
    void testCreateValidUser() {
        // Given
        User inputUser = new User(
            null, "jdoe", "John", "Doe", "john@example.com",
            "123456789", DocumentType.DNI, "12345678", "Address 1",
            null, LocalDate.of(1990, 1, 1)
        );
        
        User expectedUser = new User(
            "generated-id", "jdoe", "John", "Doe", "john@example.com",
            "123456789", DocumentType.DNI, "12345678", "Address 1",
            UserStatus.ACTIVE, LocalDate.of(1990, 1, 1)
        );

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByDocumentNumber(anyString())).thenReturn(false);
        when(userRepository.create(any(User.class), anyString())).thenReturn(expectedUser);

        // When
        User result = createUserUseCase.execute(inputUser, "password123");

        // Then
        assertNotNull(result);
        assertEquals("generated-id", result.id());
        assertEquals(UserStatus.ACTIVE, result.status());
        
        verify(userRepository).existsByUsername("jdoe");
        verify(userRepository).existsByEmail("john@example.com");
        verify(userRepository).existsByDocumentNumber("12345678");
        verify(userRepository).create(any(User.class), anyString());
    }

    @Test
    @DisplayName("Lanzar excepción si el formato del DNI es inválido")
    void testCreateUserWithInvalidDni() {
        // Given
        User inputUser = new User(
            null, "jdoe", "John", "Doe", "john@example.com",
            "123456789", DocumentType.DNI, "123", // DNI inválido (debe tener 8 dígitos)
            "Address 1", null, LocalDate.of(1990, 1, 1)
        );

        // When & Then
        InvalidDocumentException exception = assertThrows(
            InvalidDocumentException.class,
            () -> createUserUseCase.execute(inputUser, "password123")
        );

        assertTrue(exception.getMessage().contains("formato esperado"));
        verify(userRepository, never()).create(any(), any());
    }

    @Test
    @DisplayName("Lanzar excepción si el username ya existe")
    void testCreateUserWithDuplicateUsername() {
        // Given
        User inputUser = new User(
            null, "jdoe", "John", "Doe", "john@example.com",
            "123456789", DocumentType.DNI, "12345678", "Address 1",
            null, LocalDate.of(1990, 1, 1)
        );

        when(userRepository.existsByUsername("jdoe")).thenReturn(true);

        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> createUserUseCase.execute(inputUser, "password123")
        );

        assertTrue(exception.getMessage().contains("username"));
        assertTrue(exception.getMessage().contains("ya está en uso"));
        verify(userRepository, never()).create(any(), any());
    }

    @Test
    @DisplayName("Lanzar excepción si el email ya existe")
    void testCreateUserWithDuplicateEmail() {
        // Given
        User inputUser = new User(
            null, "jdoe", "John", "Doe", "john@example.com",
            "123456789", DocumentType.DNI, "12345678", "Address 1",
            null, LocalDate.of(1990, 1, 1)
        );

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> createUserUseCase.execute(inputUser, "password123")
        );

        assertTrue(exception.getMessage().contains("email"));
        assertTrue(exception.getMessage().contains("ya está registrado"));
        verify(userRepository, never()).create(any(), any());
    }

    @Test
    @DisplayName("Lanzar excepción si el documento ya existe")
    void testCreateUserWithDuplicateDocumentNumber() {
        // Given
        User inputUser = new User(
            null, "jdoe", "John", "Doe", "john@example.com",
            "123456789", DocumentType.DNI, "12345678", "Address 1",
            null, LocalDate.of(1990, 1, 1)
        );

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByDocumentNumber("12345678")).thenReturn(true);

        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> createUserUseCase.execute(inputUser, "password123")
        );

        assertTrue(exception.getMessage().contains("documento"));
        verify(userRepository, never()).create(any(), any());
    }

    @Test
    @DisplayName("Lanzar excepción si el usuario es menor de 18 años")
    void testCreateUserUnder18YearsOld() {
        // Given
        User inputUser = new User(
            null, "jdoe", "John", "Doe", "john@example.com",
            "123456789", DocumentType.DNI, "12345678", "Address 1",
            null, LocalDate.now().minusYears(15) // 15 años
        );

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByDocumentNumber(anyString())).thenReturn(false);

        // When & Then
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> createUserUseCase.execute(inputUser, "password123")
        );

        assertTrue(exception.getMessage().contains("18 años"));
        verify(userRepository, never()).create(any(), any());
    }

    @Test
    @DisplayName("Usuario con exactamente 18 años debe ser válido")
    void testCreateUserWithExactly18Years() {
        // Given
        User inputUser = new User(
            null, "jdoe", "John", "Doe", "john@example.com",
            "123456789", DocumentType.DNI, "12345678", "Address 1",
            null, LocalDate.now().minusYears(18)
        );

        User expectedUser = new User(
            "generated-id", "jdoe", "John", "Doe", "john@example.com",
            "123456789", DocumentType.DNI, "12345678", "Address 1",
            UserStatus.ACTIVE, LocalDate.now().minusYears(18)
        );

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByDocumentNumber(anyString())).thenReturn(false);
        when(userRepository.create(any(User.class), anyString())).thenReturn(expectedUser);

        // When
        User result = createUserUseCase.execute(inputUser, "password123");

        // Then
        assertNotNull(result);
        verify(userRepository).create(any(User.class), anyString());
    }

    @Test
    @DisplayName("La contraseña debe ser hasheada antes de persistir")
    void testPasswordIsHashed() {
        // Given
        User inputUser = new User(
            null, "jdoe", "John", "Doe", "john@example.com",
            "123456789", DocumentType.DNI, "12345678", "Address 1",
            null, LocalDate.of(1990, 1, 1)
        );

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByDocumentNumber(anyString())).thenReturn(false);
        when(userRepository.create(any(User.class), anyString())).thenReturn(inputUser);

        // When
        createUserUseCase.execute(inputUser, "plainPassword");

        // Then
        verify(userRepository).create(any(User.class), argThat(hashedPassword -> 
            hashedPassword != null && 
            !hashedPassword.equals("plainPassword") &&
            hashedPassword.startsWith("$2a$") // BCrypt hash starts with $2a$
        ));
    }
}
