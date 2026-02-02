package com.example.hexagonalarchitecture.users.infraestructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.hexagonalarchitecture.users.application.port.in.ActivateUserUseCase;
import com.example.hexagonalarchitecture.users.application.port.in.CreateUserUseCase;
import com.example.hexagonalarchitecture.users.application.port.in.DeactivateUserUseCase;
import com.example.hexagonalarchitecture.users.application.port.in.DeleteUserUseCase;
import com.example.hexagonalarchitecture.users.application.port.in.GetUserByIdUseCase;
import com.example.hexagonalarchitecture.users.application.port.in.GetUsersByFirstNameUseCase;
import com.example.hexagonalarchitecture.users.application.port.in.GetUsersByLastNameUseCase;
import com.example.hexagonalarchitecture.users.application.port.in.SearchUsersUseCase;
import com.example.hexagonalarchitecture.users.application.port.in.UpdateUserUseCase;
import com.example.hexagonalarchitecture.users.application.port.out.UserRepositoryPort;

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

    @Bean
    public UpdateUserUseCase updateUserUseCase(
            UserRepositoryPort userRepositoryPort
    ) {
        return new UpdateUserUseCase(userRepositoryPort);
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase(
            UserRepositoryPort userRepositoryPort
    ) {
        return new DeleteUserUseCase(userRepositoryPort);
    }

    @Bean
    public ActivateUserUseCase activateUserUseCase(
            UserRepositoryPort userRepositoryPort
    ) {
        return new ActivateUserUseCase(userRepositoryPort);
    }

    @Bean
    public DeactivateUserUseCase deactivateUserUseCase(
            UserRepositoryPort userRepositoryPort
    ) {
        return new DeactivateUserUseCase(userRepositoryPort);
    }
}