package com.mitocode.security;

import com.mitocode.model.User;
import com.mitocode.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String principal = authentication.getPrincipal().toString();
        String credentials = authentication.getCredentials().toString();

        // Si las credenciales parecen un JWT, validar token (para endpoints protegidos)
        if (credentials != null && credentials.split("\\.").length == 3) {
            String usernameFromToken = jwtUtil.extractUsername(credentials);
            if (usernameFromToken == null || !usernameFromToken.equals(principal) || jwtUtil.extractExpiration(credentials).before(new java.util.Date())) {
                return Mono.error(new BadCredentialsException("Token JWT inválido o expirado"));
            }
            // Extraer roles del token (pueden estar como String separados por coma)
            Object rolesObj = jwtUtil.extractClaim(credentials, claims -> claims.get("roles"));
            java.util.List<GrantedAuthority> authorities = new java.util.ArrayList<>();
            if (rolesObj instanceof String rolesStr) {
                for (String role : rolesStr.split(",")) {
                    authorities.add(new SimpleGrantedAuthority(role.trim()));
                }
            }
            return Mono.just(new UsernamePasswordAuthenticationToken(principal, null, authorities));
        }
        // Si no es JWT, es login clásico: validar usuario/contraseña
        return userRepo.findOneByUsername(principal)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("Usuario no encontrado")))
                .flatMap(user -> {
                    if (!user.isEnabled()) {
                        return Mono.error(new BadCredentialsException("Usuario deshabilitado"));
                    }
                    // Validar password con BCrypt
                    if (!org.springframework.security.crypto.bcrypt.BCrypt.checkpw(credentials, user.getPassword())) {
                        return Mono.error(new BadCredentialsException("Credenciales inválidas"));
                    }
                    java.util.List<GrantedAuthority> authorities = user.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority(role.getName()))
                            .collect(Collectors.toList());
                    return Mono.just(new UsernamePasswordAuthenticationToken(principal, null, authorities));
                });
    }
}
