package com.example.managerapp;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
@OpenAPIDefinition(
        info = @Info(
                title = "Manager App API",
                version = "1.0",
                description = "Documentation for the API for managing tasks, projects and users"
        )
)
@SpringBootApplication
@EnableCaching
public class ManagerAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagerAppApplication.class, args);
    }
}