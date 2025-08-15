package com.mitocode.handler;

import com.mitocode.model.Curso;
import com.mitocode.repo.CursoRepo;
import com.mitocode.dto.CursoDTO;
import org.modelmapper.ModelMapper;
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

    @Autowired
    private ModelMapper modelMapper;

    public Mono<ServerResponse> findAll(ServerRequest req) {
    Flux<CursoDTO> cursosDTO = cursoRepo.findAll()
        .map(cur -> modelMapper.map(cur, CursoDTO.class));
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(cursosDTO, CursoDTO.class);
}

    public Mono<ServerResponse> findById(ServerRequest req) {
    String id = req.pathVariable("id");
    return cursoRepo.findById(id)
            .map(cur -> modelMapper.map(cur, CursoDTO.class))
            .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(dto))
            .switchIfEmpty(ServerResponse.notFound().build());
}

    public Mono<ServerResponse> save(ServerRequest req) {
    Mono<CursoDTO> cursoDTOMono = req.bodyToMono(CursoDTO.class);
    return cursoDTOMono
        .map(dto -> modelMapper.map(dto, Curso.class))
        .flatMap(cur -> cursoRepo.save(cur))
        .map(cur -> modelMapper.map(cur, CursoDTO.class))
        .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(dto));
}

    public Mono<ServerResponse> update(ServerRequest req) {
    String id = req.pathVariable("id");
    Mono<CursoDTO> cursoDTOMono = req.bodyToMono(CursoDTO.class);
    return cursoRepo.findById(id)
            .flatMap(existing -> cursoDTOMono.doOnNext(dto -> dto.setId(id)))
            .map(dto -> modelMapper.map(dto, Curso.class))
            .flatMap(cursoRepo::save)
            .map(cur -> modelMapper.map(cur, CursoDTO.class))
            .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(dto))
            .switchIfEmpty(ServerResponse.notFound().build());
}

    public Mono<ServerResponse> delete(ServerRequest req) {
    String id = req.pathVariable("id");
    return cursoRepo.deleteById(id)
            .then(ServerResponse.noContent().build());
}
}
