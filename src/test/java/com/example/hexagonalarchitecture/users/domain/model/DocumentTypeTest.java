package com.example.hexagonalarchitecture.users.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DocumentType - Validación de formatos de documento")
class DocumentTypeTest {

    @ParameterizedTest
    @CsvSource({
        "DNI, 12345678, true",
        "DNI, 87654321, true",
        "DNI, 1234567, false",    // Menos de 8 dígitos
        "DNI, 123456789, false",  // Más de 8 dígitos
        "DNI, 1234567A, false",   // Contiene letra
    })
    @DisplayName("Validar formato de DNI (8 dígitos)")
    void testDniValidation(DocumentType type, String number, boolean expected) {
        assertEquals(expected, type.isValidNumber(number));
    }

    @ParameterizedTest
    @CsvSource({
        "CE, 123456789, true",
        "CE, 000000001, true",
        "CE, 12345678, true",     // 1-20 dígitos
        "CE, 1234567890, true",   // 1-20 dígitos
    })
    @DisplayName("Validar formato de Carné de Extranjería (1-20 dígitos)")
    void testCeValidation(DocumentType type, String number, boolean expected) {
        assertEquals(expected, type.isValidNumber(number));
    }

    @ParameterizedTest
    @CsvSource({
        "PASSPORT, ABC123456, true",
        "PASSPORT, XYZ789012, true",
        "PASSPORT, AB1234567, true",
        "PASSPORT, AB123, false",         // Muy corto
        "PASSPORT, ABCDEFGHIJ123, true",  // 6-20 alfanum
    })
    @DisplayName("Validar formato de Pasaporte (alfanumérico 6-20 caracteres)")
    void testPassportValidation(DocumentType type, String number, boolean expected) {
        assertEquals(expected, type.isValidNumber(number));
    }

    @Test
    @DisplayName("TI permite números de diferentes longitudes")
    void testTiValidation() {
        DocumentType ti = DocumentType.TI;
        assertTrue(ti.isValidNumber("12345678"));
        assertTrue(ti.isValidNumber("123456789"));
        assertTrue(ti.isValidNumber("1234567890"));
    }
}
