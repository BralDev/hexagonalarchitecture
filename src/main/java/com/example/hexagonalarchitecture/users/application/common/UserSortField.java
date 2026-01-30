package com.example.hexagonalarchitecture.users.application.common;

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