package com.mitocode.controller;

import com.mitocode.model.Matricula;
import com.mitocode.repo.MatriculaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MatriculaHandler {

    @Autowired
    private MatriculaRepo matriculaRepo;

    public Mono<ServerResponse> findAll(ServerRequest req) {
        Flux<Matricula> matriculas = matriculaRepo.findAll();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(matriculas, Matricula.class);
    }

    public Mono<ServerResponse> findById(ServerRequest req) {
        String id = req.pathVariable("id");
        return matriculaRepo.findById(id)
                .flatMap(mat -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(mat))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> save(ServerRequest req) {
        Mono<Matricula> matriculaMono = req.bodyToMono(Matricula.class);
        return matriculaMono.flatMap(matriculaRepo::save)
                .flatMap(mat -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(mat));
    }

    public Mono<ServerResponse> update(ServerRequest req) {
        String id = req.pathVariable("id");
        Mono<Matricula> matriculaMono = req.bodyToMono(Matricula.class);
        return matriculaRepo.findById(id)
                .flatMap(existing -> matriculaMono.doOnNext(mat -> mat.setId(id)))
                .flatMap(matriculaRepo::save)
                .flatMap(mat -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(mat))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest req) {
        String id = req.pathVariable("id");
        return matriculaRepo.deleteById(id)
                .then(ServerResponse.noContent().build());
    }
}
