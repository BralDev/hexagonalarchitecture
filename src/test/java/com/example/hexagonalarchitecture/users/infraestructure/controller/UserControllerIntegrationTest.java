package com.example.hexagonalarchitecture.users.infraestructure.controller;

import com.example.hexagonalarchitecture.users.domain.model.DocumentType;
import com.example.hexagonalarchitecture.users.domain.model.UserStatus;
import com.example.hexagonalarchitecture.users.infraestructure.persistence.SpringDataUserRepository;
import com.example.hexagonalarchitecture.users.infraestructure.persistence.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("UserController - Tests de integración")
class UserControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private ObjectMapper objectMapper;

    @Autowired
    private SpringDataUserRepository userRepository;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.objectMapper = new ObjectMapper().findAndRegisterModules();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /users - Crear usuario exitosamente")
    void testCreateUser() throws Exception {
        // Given
        Map<String, Object> userRequest = new HashMap<>();
        userRequest.put("username", "jdoe");
        userRequest.put("firstName", "John");
        userRequest.put("lastName", "Doe");
        userRequest.put("email", "john@example.com");
        userRequest.put("phone", "123456789");
        userRequest.put("documentType", "DNI");
        userRequest.put("documentNumber", "12345678");
        userRequest.put("address", "Test Address");
        userRequest.put("birthDate", "1990-01-01");
        userRequest.put("password", "SecurePassword123!");

        // When & Then
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("jdoe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @DisplayName("POST /users - Rechazar usuario con DNI inválido")
    void testCreateUserWithInvalidDni() throws Exception {
        // Given
        Map<String, Object> userRequest = new HashMap<>();
        userRequest.put("username", "jdoe");
        userRequest.put("firstName", "John");
        userRequest.put("lastName", "Doe");
        userRequest.put("email", "john@example.com");
        userRequest.put("documentType", "DNI");
        userRequest.put("documentNumber", "123"); // DNI inválido
        userRequest.put("birthDate", "1990-01-01");
        userRequest.put("password", "SecurePassword123!");

        // When & Then
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("formato")));
    }

    @Test
    @DisplayName("GET /users/{id} - Obtener usuario por ID")
    void testGetUserById() throws Exception {
        // Given - Crear usuario en la base de datos
        UserEntity savedUser = userRepository.save(new UserEntity(
            null, "jdoe", "hashedPassword", "John", "Doe", "john@example.com",
            "123456789", DocumentType.DNI, "12345678", "Address 1", 
            UserStatus.ACTIVE, LocalDate.of(1990, 1, 1)
        ));

        // When & Then
        mockMvc.perform(get("/users/" + savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.username").value("jdoe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @DisplayName("GET /users/{id} - Retornar 404 si usuario no existe")
    void testGetUserByIdNotFound() throws Exception {
        mockMvc.perform(get("/users/nonexistent-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /users/{id} - Actualizar usuario")
    void testUpdateUser() throws Exception {
        // Given
        UserEntity savedUser = userRepository.save(new UserEntity(
            null, "jdoe", "hashedPassword", "John", "Doe", "john@example.com",
            "123456789", DocumentType.DNI, "12345678", "Address 1",
            UserStatus.ACTIVE, LocalDate.of(1990, 1, 1)
        ));

        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("username", "jdoe");
        updateRequest.put("firstName", "Jane");
        updateRequest.put("lastName", "Smith");
        updateRequest.put("status", "ACTIVE");
        updateRequest.put("email", "jane.smith@example.com");

        // When & Then
        mockMvc.perform(put("/users/" + savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.email").value("jane.smith@example.com"));
    }

    @Test
    @DisplayName("DELETE /users/{id} - Eliminar usuario")
    void testDeleteUser() throws Exception {
        // Given
        UserEntity savedUser = userRepository.save(new UserEntity(
            null, "jdoe", "hashedPassword", "John", "Doe", "john@example.com",
            "123456789", DocumentType.DNI, "12345678", "Address 1",
            UserStatus.ACTIVE, LocalDate.of(1990, 1, 1)
        ));

        // When & Then
        mockMvc.perform(delete("/users/" + savedUser.getId()))
                .andExpect(status().isNoContent());

        // Verify - soft delete mantiene el usuario con estado DELETED
        mockMvc.perform(get("/users/" + savedUser.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("DELETED"));
    }

    @Test
    @DisplayName("GET /users - Buscar usuarios con filtros")
    void testSearchUsers() throws Exception {
        // Given - Crear varios usuarios
        userRepository.save(new UserEntity(
            null, "jdoe", "hashedPassword", "John", "Doe", "john@example.com",
            "111111111", DocumentType.DNI, "12345678", "Address 1",
            UserStatus.ACTIVE, LocalDate.of(1990, 1, 1)
        ));
        
        userRepository.save(new UserEntity(
            null, "jsmith", "hashedPassword", "Jane", "Smith", "jane@example.com",
            "222222222", DocumentType.DNI, "87654321", "Address 2",
            UserStatus.ACTIVE, LocalDate.of(1985, 5, 15)
        ));

        // When & Then
        mockMvc.perform(get("/users")
            .param("lastName", "Doe"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(1))))
            .andExpect(jsonPath("$.data[0].lastName").value("Doe"));
    }

    @Test
    @DisplayName("POST /users - Rechazar username duplicado")
    void testCreateUserWithDuplicateUsername() throws Exception {
        // Given - Usuario existente
        userRepository.save(new UserEntity(
            null, "jdoe", "hashedPassword", "John", "Doe", "john@example.com",
            "123456789", DocumentType.DNI, "12345678", "Address 1",
            UserStatus.ACTIVE, LocalDate.of(1990, 1, 1)
        ));

        Map<String, Object> userRequest = new HashMap<>();
        userRequest.put("username", "jdoe"); // Username duplicado
        userRequest.put("firstName", "Jane");
        userRequest.put("lastName", "Smith");
        userRequest.put("email", "jane@example.com");
        userRequest.put("documentType", "DNI");
        userRequest.put("documentNumber", "87654321");
        userRequest.put("birthDate", "1992-03-15");
        userRequest.put("password", "SecurePassword123!");

        // When & Then
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("username")));
    }
}
