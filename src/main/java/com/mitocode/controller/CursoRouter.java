package com.mitocode.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class CursoRouter {
    @Bean
    public RouterFunction<ServerResponse> cursoRoutes(CursoHandler handler) {
        return RouterFunctions.route()
                .GET("/api/cursos", handler::findAll)
                .GET("/api/cursos/{id}", handler::findById)
                .POST("/api/cursos", handler::save)
                .PUT("/api/cursos/{id}", handler::update)
                .DELETE("/api/cursos/{id}", handler::delete)
                .build();
    }
}
