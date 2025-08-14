package com.mitocode.controller;

import com.mitocode.model.Curso;
import com.mitocode.repo.CursoRepo;
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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Curso> findAll() {
        return cursoRepo.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Curso> findById(@PathVariable("id") String id) {
        return cursoRepo.findById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Curso> save(@RequestBody Curso curso) {
        return cursoRepo.save(curso);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Curso> update(@PathVariable("id") String id, @RequestBody Curso curso) {
        curso.setId(id);
        return cursoRepo.save(curso);
    }

    @DeleteMapping(value = "/{id}")
    public Mono<Void> delete(@PathVariable("id") String id) {
        return cursoRepo.deleteById(id);
    }
}
