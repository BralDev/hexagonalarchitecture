package com.example.hexagonal_architecture_example.infraestructure.persistance;

import org.springframework.data.jpa.domain.Specification;

public final class UserSpecifications {

    private UserSpecifications() {}

    public static Specification<UserEntity> firstNameContains(String firstName) {
        return (root, query, cb) ->
                firstName == null
                        ? null
                        : cb.like(
                                cb.lower(root.get("firstName")),
                                "%" + firstName.toLowerCase() + "%"
                        );
    }

    public static Specification<UserEntity> lastNameContains(String lastName) {
        return (root, query, cb) ->
                lastName == null
                        ? null
                        : cb.like(
                                cb.lower(root.get("lastName")),
                                "%" + lastName.toLowerCase() + "%"
                        );
    }
}
