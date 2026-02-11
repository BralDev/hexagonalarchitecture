package com.example.hexagonalarchitecture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
    info = @Info(
        title = "API de Usuarios",
        version = "1.0.0",
        description = "API REST para la gestión de usuarios."
    )
)
@SpringBootApplication
public class HexagonalArchitectureExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(HexagonalArchitectureExampleApplication.class, args);
	}

}
