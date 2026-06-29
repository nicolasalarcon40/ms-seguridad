package com.colegio.msseguridad.service;

import com.colegio.msseguridad.dto.AuthRequestDTO;
import com.colegio.msseguridad.dto.AuthResponseDTO;
import com.colegio.msseguridad.exception.UnauthorizedException;
import com.colegio.msseguridad.model.Usuario;
import com.colegio.msseguridad.repository.UsuarioRepository;
import com.colegio.msseguridad.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private Usuario usuario;
    private AuthRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario(1L, "admin", "hashedPassword123", "ROLE_ADMIN");

        requestDTO = new AuthRequestDTO();
        requestDTO.setUsername("admin");
        requestDTO.setPassword("password123");
    }

    
    @Test
    void testLogin_credencialesCorrectas_debeRetornarToken() {
        
        when(repository.findByUsername("admin")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("password123", "hashedPassword123")).thenReturn(true);
        when(jwtService.generarToken("admin", "ROLE_ADMIN")).thenReturn("token.jwt.mock");

        
        AuthResponseDTO resultado = authService.login(requestDTO);

        
        assertNotNull(resultado);
        assertEquals("token.jwt.mock", resultado.getToken());
        verify(jwtService, times(1)).generarToken("admin", "ROLE_ADMIN");
    }

    
    @Test
    void testLogin_usuarioNoExiste_debeLanzarExcepcion() {
        
        when(repository.findByUsername("admin")).thenReturn(Optional.empty());

        
        UnauthorizedException ex = assertThrows(
            UnauthorizedException.class,
            () -> authService.login(requestDTO)
        );

        assertEquals("Credenciales invalidas", ex.getMessage());
        verify(jwtService, never()).generarToken(any(), any());
    }

    
    @Test
    void testLogin_contrasenaIncorrecta_debeLanzarExcepcion() {
        // Arrange
        when(repository.findByUsername("admin")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("password123", "hashedPassword123")).thenReturn(false);

      
        UnauthorizedException ex = assertThrows(
            UnauthorizedException.class,
            () -> authService.login(requestDTO)
        );

        assertEquals("Credenciales invalidas", ex.getMessage());
        verify(jwtService, never()).generarToken(any(), any());
    }
}