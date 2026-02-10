package com.example.hexagonalarchitecture.users.infraestructure.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.hexagonalarchitecture.users.infraestructure.controller.dto.ErrorResponse;
import com.example.hexagonalarchitecture.users.infraestructure.exception.EntityNotFoundException;
import com.example.hexagonalarchitecture.users.infraestructure.exception.InvalidDocumentException;
import com.example.hexagonalarchitecture.users.infraestructure.exception.InvalidPasswordException;
import com.example.hexagonalarchitecture.users.infraestructure.exception.ValidationException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Manejador global de excepciones para la API REST de usuarios.
 * 
 * Centraliza el manejo de todas las excepciones lanzadas en los controladores
 * y use cases, devolviendo respuestas HTTP estructuradas y consistentes al cliente.
 * 
 * Cada método marca una estrategia de manejo específica para un tipo de excepción,
 * traduciendo errores de negocio y validación a códigos HTTP apropiados:
 * - 400 (BAD_REQUEST): errores de validación, documentos inválidos, passwords incorrectos
 * - 404 (NOT_FOUND): recursos no encontrados
 * - 500 (INTERNAL_SERVER_ERROR): errores inesperados del servidor
 * 
 * Las respuestas devuelven un {@link ErrorResponse} con estructura consistente:
 * timestamp, status (código HTTP), error (categoría), message (detalle), path (URI solicitada).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones {@link EntityNotFoundException}.
     * 
     * Lanzada cuando se intenta recuperar un recurso (usuario) que no existe en la base de datos.
     * Típicamente ocurre en operaciones GET, UPDATE, DELETE cuando el ID no existe.
     * 
     * Retorna:
     * - HTTP 404 (NOT_FOUND)
     * - Código de error: "ENTITY_NOT_FOUND"
     * 
     * @param ex excepción con mensaje detallado del recurso no encontrado
     * @param request solicitud HTTP para capturar el path
     * @return ResponseEntity con ErrorResponse y status 404
     * 
     * @example
     *   GET /users/uuid-inexistente
     *   Response 404: { "status": 404, "error": "ENTITY_NOT_FOUND", "message": "Usuario no encontrado con id: uuid-inexistente" }
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(
            EntityNotFoundException ex,
            HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            "ENTITY_NOT_FOUND",
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Maneja excepciones {@link InvalidDocumentException}.
     * 
     * Lanzada cuando el formato del número de documento no coincide con el patrón
     * esperado para el tipo de documento (DNI, CE, PASSPORT, TI).
     * Ocurre durante la creación de usuarios si el formato es inválido.
     * 
     * Retorna:
     * - HTTP 400 (BAD_REQUEST)
     * - Código de error: "INVALID_DOCUMENT_FORMAT"
     * 
     * @param ex excepción con detalles del tipo de documento y formato esperado
     * @param request solicitud HTTP para capturar el path
     * @return ResponseEntity con ErrorResponse y status 400
     * 
     * @example
     *   POST /users (documentNumber: "abc123" para tipo DNI)
     *   Response 400: { "status": 400, "error": "INVALID_DOCUMENT_FORMAT", "message": "El número de documento no cumple con el formato..." }
     */
    @ExceptionHandler(InvalidDocumentException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDocument(
            InvalidDocumentException ex,
            HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "INVALID_DOCUMENT_FORMAT",
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Maneja excepciones {@link InvalidPasswordException}.
     * 
     * Lanzada en dos escenarios durante cambio de contraseña:
     * 1. La contraseña actual proporcionada no coincide con la almacenada (hasheada)
     * 2. La nueva contraseña y su confirmación no coinciden
     * 
     * Retorna:
     * - HTTP 400 (BAD_REQUEST)
     * - Código de error: "INVALID_PASSWORD"
     * 
     * @param ex excepción con mensaje indicando cuál validación falló
     * @param request solicitud HTTP para capturar el path
     * @return ResponseEntity con ErrorResponse y status 400
     * 
     * @example
     *   POST /users/123/password (currentPassword incorrecta)
     *   Response 400: { "status": 400, "error": "INVALID_PASSWORD", "message": "La contraseña actual es incorrecta" }
     */
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPassword(
            InvalidPasswordException ex,
            HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "INVALID_PASSWORD",
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Maneja excepciones {@link ValidationException}.
     * 
     * Lanzada cuando ocurren errores de validación de negocio no cubiertos por
     * otros tipos de excepciones específicas (p.e., validaciones customizadas).
     * 
     * Retorna:
     * - HTTP 400 (BAD_REQUEST)
     * - Código de error: "VALIDATION_ERROR"
     * 
     * @param ex excepción con mensaje de validación
     * @param request solicitud HTTP para capturar el path
     * @return ResponseEntity con ErrorResponse y status 400
     * 
     * @example
     *   POST /users (validación customizada fallida)
     *   Response 400: { "status": 400, "error": "VALIDATION_ERROR", "message": "Validación fallida: ..." }
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            ValidationException ex,
            HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "VALIDATION_ERROR",
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Maneja excepciones {@link MethodArgumentNotValidException}.
     * 
     * Lanzada automáticamente por Spring cuando la validación de DTOs falla
     * (anotaciones como @NotBlank, @NotNull, @Email, @Size en request bodies).
     * Extrae el primer error de campo para devolver un mensaje más específico.
     * 
     * Retorna:
     * - HTTP 400 (BAD_REQUEST)
     * - Código de error: "VALIDATION_ERROR"
     * - Mensaje: "fieldName: error message" del primer campo que falló
     * 
     * @param ex excepción con detalles de validación de Spring
     * @param request solicitud HTTP para capturar el path
     * @return ResponseEntity con ErrorResponse y status 400
     * 
     * @example
     *   POST /users (firstName vacío, notificación de validación)
     *   Response 400: { "status": 400, "error": "VALIDATION_ERROR", "message": "firstName: firstName es obligatorio" }
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Error de validación");
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "VALIDATION_ERROR",
            message,
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Maneja excepciones {@link IllegalArgumentException}.
     * 
     * Excepción genérica para errores de validación de argumentos.
     * Aunque se prefieren excepciones específicas, este handler actúa como respaldo
     * para cualquier IllegalArgumentException no capturada por otros handlers.
     * 
     * Retorna:
     * - HTTP 400 (BAD_REQUEST)
     * - Código de error: "INVALID_REQUEST"
     * 
     * @param ex excepción con mensaje del error
     * @param request solicitud HTTP para capturar el path
     * @return ResponseEntity con ErrorResponse y status 400
     * 
     * @example
     *   POST /users (IllegalArgumentException no esperada)
     *   Response 400: { "status": 400, "error": "INVALID_REQUEST", "message": "..." }
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "INVALID_REQUEST",
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Maneja excepciones genéricas {@link Exception}.
     * 
     * Último nivel de defensa cuando ningún otro handler catch la excepción.
     * Devuelve un mensaje genérico sin exponer detalles internos del servidor
     * por razones de seguridad. Este handler debe ser raro si las excepciones
     * específicas están bien implementadas.
     * 
     * Retorna:
     * - HTTP 500 (INTERNAL_SERVER_ERROR)
     * - Código de error: "INTERNAL_SERVER_ERROR"
     * - Mensaje: genérico (no expone stack trace ni detalles técnicos)
     * 
     * @param ex excepción inesperada
     * @param request solicitud HTTP para capturar el path
     * @return ResponseEntity con ErrorResponse y status 500
     * 
     * @example
     *   GET /users/123 (error inesperado en la BD)
     *   Response 500: { "status": 500, "error": "INTERNAL_SERVER_ERROR", "message": "Error interno del servidor", "path": "/users/123" }
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "INTERNAL_SERVER_ERROR",
            "Error interno del servidor",
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
