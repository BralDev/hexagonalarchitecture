package com.example.hexagonalarchitecture.users.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User - Modelo de dominio")
class UserTest {

    @Test
    @DisplayName("Crear usuario con todos los campos")
    void testCreateUserWithAllFields() {
        User user = new User(
            "1",
            "jdoe",
            "John",
            "Doe",
            "john@example.com",
            "123456789",
            DocumentType.DNI,
            "12345678",
            "Calle Principal 123",
            UserStatus.ACTIVE,
            LocalDate.of(1990, 1, 1)
        );

        assertNotNull(user);
        assertEquals("1", user.id());
        assertEquals("jdoe", user.username());
        assertEquals("John", user.firstName());
        assertEquals("Doe", user.lastName());
        assertEquals("john@example.com", user.email());
        assertEquals(DocumentType.DNI, user.documentType());
        assertEquals("12345678", user.documentNumber());
        assertEquals(UserStatus.ACTIVE, user.status());
    }

    @Test
    @DisplayName("Usuarios con mismo ID son iguales (record equality)")
    void testUserEquality() {
        User user1 = new User(
            "1", "jdoe", "John", "Doe", "john@example.com",
            "123456789", DocumentType.DNI, "12345678", "Address 1",
            UserStatus.ACTIVE, LocalDate.of(1990, 1, 1)
        );

        User user2 = new User(
            "1", "jdoe", "John", "Doe", "john@example.com",
            "123456789", DocumentType.DNI, "12345678", "Address 1",
            UserStatus.ACTIVE, LocalDate.of(1990, 1, 1)
        );

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    @DisplayName("Usuarios con diferente ID no son iguales")
    void testUserInequality() {
        User user1 = new User(
            "1", "jdoe", "John", "Doe", "john@example.com",
            "123456789", DocumentType.DNI, "12345678", "Address 1",
            UserStatus.ACTIVE, LocalDate.of(1990, 1, 1)
        );

        User user2 = new User(
            "2", "jdoe", "John", "Doe", "john@example.com",
            "123456789", DocumentType.DNI, "12345678", "Address 1",
            UserStatus.ACTIVE, LocalDate.of(1990, 1, 1)
        );

        assertNotEquals(user1, user2);
    }
}
