package com.example.hexagonalarchitecture.users.domain.model;

/**
 * Enum que representa los tipos de documentos de identidad válidos
 */
public enum DocumentType {
    DNI("Documento Nacional de Identidad", "^[0-9]{8}$"),
    CE("Cédula de Extranjería", "^[0-9]{1,20}$"),
    PASSPORT("Pasaporte", "^[A-Z0-9]{6,20}$"),
    TI("Tarjeta de Identidad", "^[0-9]{1,20}$");

    private final String description;
    private final String pattern;  // Regex para validación básica

    DocumentType(String description, String pattern) {
        this.description = description;
        this.pattern = pattern;
    }

    public String getDescription() {
        return description;
    }

    public String getPattern() {
        return pattern;
    }

    /**
     * Valida que el numero de documento coincida con el patrón esperado
     */
    public boolean isValidNumber(String number) {
        return number != null && number.matches(this.pattern);
    }
}
