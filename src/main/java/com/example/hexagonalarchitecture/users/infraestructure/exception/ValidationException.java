package com.example.hexagonalarchitecture.users.infraestructure.exception;

/**
 * Excepción de negocio para violaciones de reglas de validación complejas.
 * <p>
 * Propósito: Señalar que se violó una regla de negocio que requiere lógica de validación
 * más allá de simples checks de formato (ej: unicidad de datos, restricciones de edad,
 * protección de recursos especiales).
 * <p>
 * Uso: Para validaciones que requieren consultas a base de datos o lógica de negocio compleja.
 * Para validaciones simples de formato usar excepciones específicas (InvalidDocumentException, etc.)
 * o anotaciones de Spring (@NotBlank, @Email, etc.).
 * <p>
 * Manejo: Ver {@link com.example.hexagonalarchitecture.users.infraestructure.controller.GlobalExceptionHandler#handleValidation}
 * para detalles sobre la respuesta HTTP y formato de error.
 */
public class ValidationException extends RuntimeException {
    
    /**
     * Constructor con mensaje descriptivo.
     * 
     * @param message mensaje que describe qué regla de negocio se violó
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa original.
     * 
     * @param message mensaje descriptivo del error
     * @param cause excepción que causó este error
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
