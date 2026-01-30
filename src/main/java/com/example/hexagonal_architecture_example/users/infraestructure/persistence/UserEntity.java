package com.example.hexagonal_architecture_example.users.infraestructure.persistence;

import java.time.LocalDate;

import com.example.hexagonal_architecture_example.users.domain.model.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private UserStatus status;

    @Column(name = "birth_date", nullable = true)
    private LocalDate birthDate;

    protected UserEntity() {
        // Requerido por JPA
    }

    public UserEntity(Long id, String firstName, String lastName, UserStatus status, LocalDate birthDate) {
         this.id = id;
         this.firstName = firstName;
         this.lastName = lastName;
         this.status = status;
         this.birthDate = birthDate;
    }

    public Long id() {
        return id;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public UserStatus status() {
        return status;
    }

    public LocalDate birthDate() {
        return birthDate;
    }
}

