package com.example.hexagonalarchitecture.users.infraestructure.exception;

/**
 * Excepción de negocio que indica que un usuario no existe en el sistema.
 * <p>
 * Propósito: Señalar que se intentó acceder a un recurso (usuario) utilizando
 * un identificador que no está registrado en la base de datos.
 * <p>
 * Manejo: Ver {@link com.example.hexagonalarchitecture.users.infraestructure.controller.GlobalExceptionHandler#handleEntityNotFound}
 * para detalles sobre la respuesta HTTP y formato de error.
 */
public class EntityNotFoundException extends RuntimeException {
    
    /**
     * Constructor con mensaje descriptivo.
     * 
     * @param message mensaje que describe qué entidad no fue encontrada y por qué criterio
     */
    public EntityNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa original.
     * 
     * @param message mensaje descriptivo del error
     * @param cause excepción que causó este error
     */
    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
