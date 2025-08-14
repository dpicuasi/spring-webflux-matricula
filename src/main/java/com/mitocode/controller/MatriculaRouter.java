package com.mitocode.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class MatriculaRouter {
    @Bean
    public RouterFunction<ServerResponse> matriculaRoutes(MatriculaHandler handler) {
        return RouterFunctions.route()
                .GET("/api/matriculas", handler::findAll)
                .GET("/api/matriculas/{id}", handler::findById)
                .POST("/api/matriculas", handler::save)
                .PUT("/api/matriculas/{id}", handler::update)
                .DELETE("/api/matriculas/{id}", handler::delete)
                .build();
    }
}
