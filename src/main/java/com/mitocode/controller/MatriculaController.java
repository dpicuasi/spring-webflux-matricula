package com.mitocode.controller;

import com.mitocode.dto.MatriculaDTO;
import com.mitocode.model.Matricula;
import com.mitocode.repo.MatriculaRepo;
import com.mitocode.repo.EstudianteRepo;
import com.mitocode.repo.CursoRepo;
import com.mitocode.model.Estudiante;
import com.mitocode.model.Curso;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v2/matriculas")
public class MatriculaController {

    @Autowired
    private MatriculaRepo matriculaRepo;
    @Autowired
    private EstudianteRepo estudianteRepo;
    @Autowired
    private CursoRepo cursoRepo;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<MatriculaDTO> findAll() {
        return matriculaRepo.findAll()
                .map(mat -> modelMapper.map(mat, MatriculaDTO.class));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<MatriculaDTO> findById(@PathVariable("id") String id) {
        return matriculaRepo.findById(id)
                .map(mat -> modelMapper.map(mat, MatriculaDTO.class));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<MatriculaDTO> save(@RequestBody MatriculaDTO dto) {
        // Buscar estudiante por id
        Mono<Estudiante> estudianteMono = estudianteRepo.findById(dto.getEstudiante().getId());
        // Buscar todos los cursos por sus ids
        Mono<List<Curso>> cursosMono = Flux.fromIterable(dto.getCursos())
            .flatMap(cursoDTO -> cursoRepo.findById(cursoDTO.getId()))
            .collectList();

        return Mono.zip(estudianteMono, cursosMono)
            .flatMap(tuple -> {
                Matricula matricula = new Matricula();
                matricula.setEstudiante(tuple.getT1());
                matricula.setCursos(tuple.getT2());
                matricula.setFechaMatricula(LocalDateTime.now());
                return matriculaRepo.save(matricula);
            })
            .map(mat -> modelMapper.map(mat, MatriculaDTO.class));
    }

    @DeleteMapping(value = "/{id}")
    public Mono<Void> delete(@PathVariable("id") String id) {
        return matriculaRepo.deleteById(id);
    }
}
