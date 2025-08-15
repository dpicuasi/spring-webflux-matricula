package com.mitocode.handler;

import com.mitocode.model.Matricula;
import com.mitocode.repo.MatriculaRepo;
import com.mitocode.dto.MatriculaDTO;
import org.modelmapper.ModelMapper;
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

    @Autowired
    private ModelMapper modelMapper;

    public Mono<ServerResponse> findAll(ServerRequest req) {
    Flux<MatriculaDTO> matriculasDTO = matriculaRepo.findAll()
        .map(mat -> modelMapper.map(mat, MatriculaDTO.class));
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(matriculasDTO, MatriculaDTO.class);
}

    public Mono<ServerResponse> findById(ServerRequest req) {
    String id = req.pathVariable("id");
    return matriculaRepo.findById(id)
            .map(mat -> modelMapper.map(mat, MatriculaDTO.class))
            .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(dto))
            .switchIfEmpty(ServerResponse.notFound().build());
}

    public Mono<ServerResponse> save(ServerRequest req) {
    Mono<MatriculaDTO> matriculaDTOMono = req.bodyToMono(MatriculaDTO.class);
    return matriculaDTOMono
        .map(dto -> modelMapper.map(dto, Matricula.class))
        .flatMap(mat -> matriculaRepo.save(mat))
        .map(mat -> modelMapper.map(mat, MatriculaDTO.class))
        .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(dto));
}

    public Mono<ServerResponse> update(ServerRequest req) {
    String id = req.pathVariable("id");
    Mono<MatriculaDTO> matriculaDTOMono = req.bodyToMono(MatriculaDTO.class);
    return matriculaRepo.findById(id)
            .flatMap(existing -> matriculaDTOMono.doOnNext(dto -> dto.setId(id)))
            .map(dto -> modelMapper.map(dto, Matricula.class))
            .flatMap(matriculaRepo::save)
            .map(mat -> modelMapper.map(mat, MatriculaDTO.class))
            .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(dto))
            .switchIfEmpty(ServerResponse.notFound().build());
}

    public Mono<ServerResponse> delete(ServerRequest req) {
    String id = req.pathVariable("id");
    return matriculaRepo.deleteById(id)
            .then(ServerResponse.noContent().build());
}
}
