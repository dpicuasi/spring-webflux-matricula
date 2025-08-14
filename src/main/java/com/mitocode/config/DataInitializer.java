package com.mitocode.config;

import com.mitocode.model.Role;
import com.mitocode.model.User;
import com.mitocode.repo.RoleRepo;
import com.mitocode.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCrypt;
import reactor.core.publisher.Flux;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepo roleRepo;
    private final UserRepo userRepo;

    @Bean
    CommandLineRunner initData() {
        return args -> {
            roleRepo.deleteAll().thenMany(
                    Flux.just("ROLE_ADMIN", "ROLE_USER")
                            .map(name -> {
                                Role r = new Role();
                                r.setName(name);
                                return r;
                            })
                            .flatMap(roleRepo::save)
            ).thenMany(
                    userRepo.deleteAll()
            ).thenMany(
                    roleRepo.findAll().collectList().flatMapMany(roles -> {
                        Role adminRole = roles.stream().filter(r -> r.getName().equals("ROLE_ADMIN")).findFirst().orElse(null);
                        Role userRole = roles.stream().filter(r -> r.getName().equals("ROLE_USER")).findFirst().orElse(null);
                        User admin = new User();
                        admin.setUsername("admin");
                        admin.setPassword(BCrypt.hashpw("admin123", BCrypt.gensalt()));
                        admin.setEnabled(true);
                        admin.setRoles(List.of(adminRole, userRole));

                        User user = new User();
                        user.setUsername("user");
                        user.setPassword(BCrypt.hashpw("user123", BCrypt.gensalt()));
                        user.setEnabled(true);
                        user.setRoles(List.of(userRole));

                        return userRepo.saveAll(List.of(admin, user));
                    })
            ).subscribe();
        };
    }
}
