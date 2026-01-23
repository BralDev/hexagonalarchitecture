package com.example.hexagonal_architecture_example.application.common;

public enum UserSortField {

    ID("id"),
    FIRST_NAME("firstName"),
    LAST_NAME("lastName");

    private final String column;

    UserSortField(String column) {
        this.column = column;
    }

    public String column() {
        return column;
    }
}