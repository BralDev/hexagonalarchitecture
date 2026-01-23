package com.example.hexagonal_architecture_example.infraestructure.persistance;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SpringDataUserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
    Page<UserEntity> findByFirstNameContainingIgnoreCase(String firstName, Pageable pageable);
    Page<UserEntity> findByLastNameContainingIgnoreCase(String lastName, Pageable pageable);
}