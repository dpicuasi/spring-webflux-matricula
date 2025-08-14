package com.mitocode.repo;

import com.mitocode.model.Matricula;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MatriculaRepo extends ReactiveMongoRepository<Matricula, String> {
}
