package com.mitocode.repo;

import com.mitocode.model.Estudiante;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface EstudianteRepo extends ReactiveMongoRepository<Estudiante, String> {
    Flux<Estudiante> findAllByOrderByEdadAsc();
    Flux<Estudiante> findAllByOrderByEdadDesc();
}
