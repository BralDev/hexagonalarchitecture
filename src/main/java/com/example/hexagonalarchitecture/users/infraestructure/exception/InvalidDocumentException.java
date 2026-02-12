package com.example.hexagonalarchitecture.users.infraestructure.exception;

/**
 * Excepción de negocio que indica formato inválido de documento de identidad.
 * <p>
 * Propósito: Señalar que el número de documento no cumple con el patrón regex
 * definido para su tipo (DNI: 8 dígitos, CE: 1-20 numéricos, PASSPORT: 6-20 alfanuméricos, TI: 1-20 numéricos).
 * La validación se realiza mediante {@link com.example.hexagonalarchitecture.users.domain.model.DocumentType#isValidNumber}.
 * <p>
 * Manejo: Ver {@link com.example.hexagonalarchitecture.users.infraestructure.controller.GlobalExceptionHandler#handleInvalidDocument}
 * para detalles sobre la respuesta HTTP y formato de error.
 */
public class InvalidDocumentException extends RuntimeException {
    
    /**
     * Constructor con mensaje descriptivo.
     * 
     * @param message mensaje que describe el tipo de documento esperado y el formato correcto
     */
    public InvalidDocumentException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa original.
     * 
     * @param message mensaje descriptivo del error
     * @param cause excepción que causó este error
     */
    public InvalidDocumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
