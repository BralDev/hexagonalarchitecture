package com.example.hexagonalarchitecture.users.application.common;

public enum UserSortField {

    ID("id"),    
    LAST_NAME("lastName"),
    DOCUMENT_NUMBER("documentNumber");

    private final String column;

    UserSortField(String column) {
        this.column = column;
    }

    public String column() {
        return column;
    }
}