package com.example.hexagonalarchitecture.users.infraestructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SpringDataUserRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    boolean existsByDocumentNumber(String documentNumber);

}