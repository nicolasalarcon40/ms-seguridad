package com.colegio.msseguridad.controller;

import com.colegio.msseguridad.dto.AuthRequestDTO;
import com.colegio.msseguridad.dto.AuthResponseDTO;
import com.colegio.msseguridad.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth") 
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponseDTO login(@RequestBody AuthRequestDTO request) {
        return authService.login(request);
    }
}