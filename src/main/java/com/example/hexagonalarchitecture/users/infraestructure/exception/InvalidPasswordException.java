package com.example.hexagonalarchitecture.users.infraestructure.exception;

/**
 * Excepción de negocio que indica error en validación de contraseñas.
 * <p>
 * Propósito: Señalar que las contraseñas proporcionadas no cumplen con los criterios
 * de seguridad requeridos (contraseña actual incorrecta o nueva contraseña no coincide con confirmación).
 * <p>
 * Manejo: Ver {@link com.example.hexagonalarchitecture.users.infraestructure.controller.GlobalExceptionHandler#handleInvalidPassword}
 * para detalles sobre la respuesta HTTP y formato de error.
 */
public class InvalidPasswordException extends RuntimeException {
    
    /**
     * Constructor con mensaje descriptivo.
     * 
     * @param message mensaje que describe qué validación de password falló
     */
    public InvalidPasswordException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa original.
     * 
     * @param message mensaje descriptivo del error
     * @param cause excepción que causó este error
     */
    public InvalidPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
