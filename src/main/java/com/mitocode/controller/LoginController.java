package com.mitocode.controller;

import com.mitocode.security.AuthRequest;
import com.mitocode.security.AuthResponse;
import com.mitocode.security.ErrorLogin;
import com.mitocode.security.JwtUtil;
import com.mitocode.model.User;
import com.mitocode.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Date;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final JwtUtil jwtUtil;
    private final UserRepo userRepo;

    @PostMapping("/login")
    public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest authRequest) {
        return userRepo.findOneByUsername(authRequest.getUsername())
                .map(user -> {
                    if (user.isEnabled() && BCrypt.checkpw(authRequest.getPassword(), user.getPassword())) {
                        // Extrae roles como string para el token
                        String roles = user.getRoles() != null && !user.getRoles().isEmpty() ?
                                String.join(",", user.getRoles().stream().map(r -> r.getName()).toList()) : "";
                        String token = jwtUtil.generateToken(user.getUsername(), roles);
                        return ResponseEntity.ok(new AuthResponse(token));
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(new ErrorLogin("Bad Credentials"));
                    }
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorLogin("User not found")));
    }
}
