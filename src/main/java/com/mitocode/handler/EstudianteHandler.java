package com.mitocode.handler;

import com.mitocode.model.Estudiante;
import com.mitocode.repo.EstudianteRepo;
import com.mitocode.dto.EstudianteDTO;
import org.modelmapper.ModelMapper;
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
private final ModelMapper modelMapper;

    @Autowired
    public EstudianteHandler(EstudianteRepo estudianteRepo, ModelMapper modelMapper) {
        this.estudianteRepo = estudianteRepo;
        this.modelMapper = modelMapper;
    }

    public Mono<ServerResponse> findAll(ServerRequest req) {
    Flux<EstudianteDTO> estudiantesDTO = estudianteRepo.findAll()
        .map(est -> modelMapper.map(est, EstudianteDTO.class));
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(estudiantesDTO, EstudianteDTO.class);
}

    public Mono<ServerResponse> findById(ServerRequest req) {
    String id = req.pathVariable("id");
    return estudianteRepo.findById(id)
            .map(est -> modelMapper.map(est, EstudianteDTO.class))
            .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(dto))
            .switchIfEmpty(ServerResponse.notFound().build());
}

    public Mono<ServerResponse> save(ServerRequest req) {
    Mono<EstudianteDTO> estudianteDTOMono = req.bodyToMono(EstudianteDTO.class);
    return estudianteDTOMono
        .map(dto -> modelMapper.map(dto, Estudiante.class))
        .flatMap(est -> estudianteRepo.save(est))
        .map(est -> modelMapper.map(est, EstudianteDTO.class))
        .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(dto));
}

    public Mono<ServerResponse> update(ServerRequest req) {
    String id = req.pathVariable("id");
    Mono<EstudianteDTO> estudianteDTOMono = req.bodyToMono(EstudianteDTO.class);
    return estudianteRepo.findById(id)
            .flatMap(existing -> estudianteDTOMono.doOnNext(dto -> dto.setId(id)))
            .map(dto -> modelMapper.map(dto, Estudiante.class))
            .flatMap(estudianteRepo::save)
            .map(est -> modelMapper.map(est, EstudianteDTO.class))
            .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(dto))
            .switchIfEmpty(ServerResponse.notFound().build());
}

    public Mono<ServerResponse> delete(ServerRequest req) {
        String id = req.pathVariable("id");
        return estudianteRepo.deleteById(id)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> findAllByEdadAsc(ServerRequest req) {
    Flux<EstudianteDTO> estudiantesDTO = estudianteRepo.findAllByOrderByEdadAsc()
        .map(est -> modelMapper.map(est, EstudianteDTO.class));
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(estudiantesDTO, EstudianteDTO.class);
}

    public Mono<ServerResponse> findAllByEdadDesc(ServerRequest req) {
    Flux<EstudianteDTO> estudiantesDTO = estudianteRepo.findAllByOrderByEdadDesc()
        .map(est -> modelMapper.map(est, EstudianteDTO.class));
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(estudiantesDTO, EstudianteDTO.class);
}
}
