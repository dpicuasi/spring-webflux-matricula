package com.mitocode.handler;

import com.mitocode.dto.CursoDTO;
import com.mitocode.dto.EstudianteDTO;
import com.mitocode.model.Curso;
import com.mitocode.model.Estudiante;
import com.mitocode.model.Matricula;
import com.mitocode.repo.CursoRepo;
import com.mitocode.repo.EstudianteRepo;
import com.mitocode.repo.MatriculaRepo;
import com.mitocode.dto.MatriculaDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class MatriculaHandler {

    @Autowired
    private MatriculaRepo matriculaRepo;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CursoRepo cursoRepo;
    @Autowired
    private EstudianteRepo estudianteRepo;

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
        .flatMap(dto -> {
            Mono<Estudiante> estudianteMono = estudianteRepo.findById(dto.getEstudiante().getId());
            Mono<java.util.List<Curso>> cursosMono = Flux.fromIterable(dto.getCursos())
                .flatMap(cursoDTO -> cursoRepo.findById(cursoDTO.getId()))
                .collectList();
            return Mono.zip(estudianteMono, cursosMono)
                .flatMap(tuple -> {
                    Matricula matricula = new Matricula();
                    matricula.setEstudiante(tuple.getT1());
                    matricula.setCursos(tuple.getT2());
                    matricula.setFechaMatricula(LocalDateTime.now());
                    return matriculaRepo.save(matricula);
                });
        })
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
