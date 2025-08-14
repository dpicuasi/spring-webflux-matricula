package com.mitocode.repo;

import com.mitocode.model.Curso;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CursoRepo extends ReactiveMongoRepository<Curso, String> {
}
