package com.mitocode.controller;

import com.mitocode.model.Curso;
import com.mitocode.repo.CursoRepo;
import com.mitocode.dto.CursoDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v2/cursos")
public class CursoController {

    @Autowired
    private CursoRepo cursoRepo;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<CursoDTO> findAll() {
        return cursoRepo.findAll()
                .map(cur -> modelMapper.map(cur, CursoDTO.class));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<CursoDTO> findById(@PathVariable("id") String id) {
        return cursoRepo.findById(id)
                .map(cur -> modelMapper.map(cur, CursoDTO.class));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<CursoDTO> save(@RequestBody CursoDTO cursoDTO) {
        return Mono.just(cursoDTO)
                .map(dto -> modelMapper.map(dto, Curso.class))
                .flatMap(cursoRepo::save)
                .map(cur -> modelMapper.map(cur, CursoDTO.class));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<CursoDTO> update(@PathVariable("id") String id, @RequestBody CursoDTO cursoDTO) {
        cursoDTO.setId(id);
        return Mono.just(cursoDTO)
                .map(dto -> modelMapper.map(dto, Curso.class))
                .flatMap(cursoRepo::save)
                .map(cur -> modelMapper.map(cur, CursoDTO.class));
    }

    @DeleteMapping(value = "/{id}")
    public Mono<Void> delete(@PathVariable("id") String id) {
        return cursoRepo.deleteById(id);
    }
}
