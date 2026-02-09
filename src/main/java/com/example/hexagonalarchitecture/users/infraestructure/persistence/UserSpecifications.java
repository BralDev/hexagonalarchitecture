package com.example.hexagonalarchitecture.users.infraestructure.persistence;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.example.hexagonalarchitecture.users.domain.model.UserStatus;

public final class UserSpecifications {

        private UserSpecifications() {
        }

        public static Specification<UserEntity> lastNameContains(String lastName) {
                return (root, query, cb) -> lastName == null
                                ? null
                                : cb.like(
                                                cb.lower(root.get("lastName")),
                                                "%" + lastName.toLowerCase() + "%");
        }

        public static Specification<UserEntity> documentNumberContains(String documentNumber) {
                return (root, query, cb) -> documentNumber == null
                                ? null
                                : cb.like(
                                                cb.lower(root.get("documentNumber")),
                                                "%" + documentNumber.toLowerCase() + "%");
        }

        public static Specification<UserEntity> hasStatus(UserStatus status) {
                return (root, query, cb) -> status == null
                                ? null
                                : cb.equal(root.get("status"), status);
        }

        public static Specification<UserEntity> birthDateFrom(LocalDate from) {
                return (root, query, cb) -> from == null
                                ? null
                                : cb.greaterThanOrEqualTo(root.get("birthDate"), from);
        }

        public static Specification<UserEntity> birthDateTo(LocalDate to) {
                return (root, query, cb) -> to == null
                                ? null
                                : cb.lessThanOrEqualTo(root.get("birthDate"), to);
        }        
}