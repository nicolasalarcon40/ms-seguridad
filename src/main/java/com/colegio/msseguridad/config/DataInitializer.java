package com.colegio.msseguridad.config;

import com.colegio.msseguridad.model.Usuario;
import com.colegio.msseguridad.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            log.info(">>> Usuarios de seguridad ya cargados. Se omite inicializacion.");
            return;
        }

        log.info(">>> Cargando usuario administrador por defecto...");
        Usuario admin = new Usuario();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123")); 
        admin.setRol("ADMIN");

        repository.save(admin);
        log.info(">>> Usuario administrador (admin/admin123) creado exitosamente.");
    }
}