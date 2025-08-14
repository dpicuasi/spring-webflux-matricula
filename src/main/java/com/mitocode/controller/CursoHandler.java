package com.mitocode.controller;

import com.mitocode.model.Curso;
import com.mitocode.repo.CursoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CursoHandler {

    @Autowired
    private CursoRepo cursoRepo;

    public Mono<ServerResponse> findAll(ServerRequest req) {
        Flux<Curso> cursos = cursoRepo.findAll();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(cursos, Curso.class);
    }

    public Mono<ServerResponse> findById(ServerRequest req) {
        String id = req.pathVariable("id");
        return cursoRepo.findById(id)
                .flatMap(cur -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(cur))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> save(ServerRequest req) {
        Mono<Curso> cursoMono = req.bodyToMono(Curso.class);
        return cursoMono.flatMap(cur -> cursoRepo.save(cur))
                .flatMap(cur -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(cur));
    }

    public Mono<ServerResponse> update(ServerRequest req) {
        String id = req.pathVariable("id");
        Mono<Curso> cursoMono = req.bodyToMono(Curso.class);
        return cursoRepo.findById(id)
                .flatMap(existing -> cursoMono.doOnNext(cur -> cur.setId(id)))
                .flatMap(cursoRepo::save)
                .flatMap(cur -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(cur))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest req) {
        String id = req.pathVariable("id");
        return cursoRepo.deleteById(id)
                .then(ServerResponse.noContent().build());
    }
}
