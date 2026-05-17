package com.colegio.msseguridad.service;

import com.colegio.msseguridad.dto.AuthRequestDTO;
import com.colegio.msseguridad.dto.AuthResponseDTO;
import com.colegio.msseguridad.exception.UnauthorizedException;
import com.colegio.msseguridad.model.Usuario;
import com.colegio.msseguridad.repository.UsuarioRepository;
import com.colegio.msseguridad.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponseDTO login(AuthRequestDTO request) {
        log.info(">>> Iniciando intento de login para el usuario: {}", request.getUsername());

  
        Usuario usuario = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    log.warn(">>> Fallo de login: Usuario '{}' no encontrado", request.getUsername());
                    return new UnauthorizedException("Credenciales invalidas");
                });

  
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            log.warn(">>> Fallo de login: Contrasena incorrecta para '{}'", request.getUsername());
            throw new UnauthorizedException("Credenciales invalidas");
        }

   
        String token = jwtService.generarToken(usuario.getUsername(), usuario.getRol());
        log.info(">>> Autenticacion exitosa. Token generado para: {}", request.getUsername());

        return new AuthResponseDTO(token);
    }
}