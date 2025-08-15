package com.mitocode.controller;

import com.mitocode.handler.EstudianteHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class EstudianteRouter {
    @Bean
    public RouterFunction<ServerResponse> estudianteRoutes(EstudianteHandler handler) {
        return RouterFunctions.route()
                .GET("/api/estudiantes", handler::findAll)
                .GET("/api/estudiantes/{id}", handler::findById)
                .GET("/api/estudiantes/ordenados/asc", handler::findAllByEdadAsc)
                .GET("/api/estudiantes/ordenados/desc", handler::findAllByEdadDesc)
                .POST("/api/estudiantes", handler::save)
                .PUT("/api/estudiantes/{id}", handler::update)
                .DELETE("/api/estudiantes/{id}", handler::delete)
                .build();
    }
}
