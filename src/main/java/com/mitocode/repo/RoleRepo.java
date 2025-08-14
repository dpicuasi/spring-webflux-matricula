package com.mitocode.repo;

import com.mitocode.model.Role;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface RoleRepo extends ReactiveMongoRepository<Role, String> {
}
