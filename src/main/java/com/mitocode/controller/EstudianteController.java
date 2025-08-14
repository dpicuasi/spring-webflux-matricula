package com.mitocode.controller;

import com.mitocode.model.Estudiante;
import com.mitocode.repo.EstudianteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v2/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteRepo estudianteRepo;

    @GetMapping("/ordenados/asc")
    public Flux<Estudiante> getAllEstudiantesByEdadAsc() {
        return estudianteRepo.findAllByOrderByEdadAsc();
    }

    @GetMapping("/ordenados/desc")
    public Flux<Estudiante> getAllEstudiantesByEdadDesc() {
        return estudianteRepo.findAllByOrderByEdadDesc();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Estudiante> findAll() {
        return estudianteRepo.findAll();
    }

    @GetMapping(value = "/asc", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Estudiante> findAllByEdadAsc() {
        return estudianteRepo.findAllByOrderByEdadAsc();
    }

    @GetMapping(value = "/desc", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Estudiante> findAllByEdadDesc() {
        return estudianteRepo.findAllByOrderByEdadDesc();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Estudiante> findById(@PathVariable("id") String id) {
        return estudianteRepo.findById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Estudiante> save(@RequestBody Estudiante estudiante) {
        return estudianteRepo.save(estudiante);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Estudiante> update(@PathVariable("id") String id, @RequestBody Estudiante estudiante) {
        estudiante.setId(id);
        return estudianteRepo.save(estudiante);
    }

    @DeleteMapping(value = "/{id}")
    public Mono<Void> delete(@PathVariable("id") String id) {
        return estudianteRepo.deleteById(id);
    }
}
