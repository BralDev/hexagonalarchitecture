package com.example.hexagonal_architecture_example.users.infraestructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.hexagonal_architecture_example.users.application.port.in.CreateUserUseCase;
import com.example.hexagonal_architecture_example.users.application.port.in.GetUserByIdUseCase;
import com.example.hexagonal_architecture_example.users.application.port.in.GetUsersByFirstNameUseCase;
import com.example.hexagonal_architecture_example.users.application.port.in.GetUsersByLastNameUseCase;
import com.example.hexagonal_architecture_example.users.application.port.in.SearchUsersUseCase;
import com.example.hexagonal_architecture_example.users.application.port.out.UserRepositoryPort;

@Configuration
public class UserUseCaseConfig {

    @Bean
    public CreateUserUseCase createUserUseCase(
            UserRepositoryPort userRepositoryPort
    ) {
        return new CreateUserUseCase(userRepositoryPort);
    }

    @Bean
    public GetUserByIdUseCase getUserByIdUseCase(
            UserRepositoryPort userRepositoryPort
    ) {
        return new GetUserByIdUseCase(userRepositoryPort);
    }

    @Bean
    public GetUsersByFirstNameUseCase getUsersByFirstNameUseCase(
            UserRepositoryPort userRepositoryPort
    ) {
        return new GetUsersByFirstNameUseCase(userRepositoryPort);
    }

    @Bean
    public GetUsersByLastNameUseCase getUsersByLastNameUseCase(
            UserRepositoryPort userRepositoryPort
    ) {
        return new GetUsersByLastNameUseCase(userRepositoryPort);
    }

    @Bean
    public SearchUsersUseCase searchUsersUseCase(
            UserRepositoryPort userRepositoryPort
    ) {
        return new SearchUsersUseCase(userRepositoryPort);
    }
}