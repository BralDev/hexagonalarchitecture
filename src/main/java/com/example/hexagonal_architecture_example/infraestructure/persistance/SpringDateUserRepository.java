package com.example.hexagonal_architecture_example.infraestructure.persistance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDateUserRepository extends JpaRepository<UserEntity, Long>{
    List<UserEntity> findByFirstName(String firstName);
    List<UserEntity> findByLastName(String lastName);
}
