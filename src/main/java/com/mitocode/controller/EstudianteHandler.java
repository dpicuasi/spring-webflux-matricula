package com.mitocode.controller;

import com.mitocode.model.Estudiante;
import com.mitocode.repo.EstudianteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class EstudianteHandler {

    private final EstudianteRepo estudianteRepo;

    @Autowired
    public EstudianteHandler(EstudianteRepo estudianteRepo) {
        this.estudianteRepo = estudianteRepo;
    }

    public Mono<ServerResponse> findAll(ServerRequest req) {
        Flux<Estudiante> estudiantes = estudianteRepo.findAll();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(estudiantes, Estudiante.class);
    }

    public Mono<ServerResponse> findById(ServerRequest req) {
        String id = req.pathVariable("id");
        return estudianteRepo.findById(id)
                .flatMap(est -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(est))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> save(ServerRequest req) {
        Mono<Estudiante> estudianteMono = req.bodyToMono(Estudiante.class);
        return estudianteMono.flatMap(est -> estudianteRepo.save(est))
                .flatMap(est -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(est));
    }

    public Mono<ServerResponse> update(ServerRequest req) {
        String id = req.pathVariable("id");
        Mono<Estudiante> estudianteMono = req.bodyToMono(Estudiante.class);
        return estudianteRepo.findById(id)
                .flatMap(existing -> estudianteMono.doOnNext(est -> est.setId(id)))
                .flatMap(estudianteRepo::save)
                .flatMap(est -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(est))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest req) {
        String id = req.pathVariable("id");
        return estudianteRepo.deleteById(id)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> findAllByEdadAsc(ServerRequest req) {
        Flux<Estudiante> estudiantes = estudianteRepo.findAllByOrderByEdadAsc();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(estudiantes, Estudiante.class);
    }

    public Mono<ServerResponse> findAllByEdadDesc(ServerRequest req) {
        Flux<Estudiante> estudiantes = estudianteRepo.findAllByOrderByEdadDesc();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(estudiantes, Estudiante.class);
    }
}
