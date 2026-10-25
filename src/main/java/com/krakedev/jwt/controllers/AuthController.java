package com.krakedev.jwt.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krakedev.jwt.entidades.Usuario;
import com.krakedev.jwt.services.UsuarioService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            Usuario registrado = usuarioService.registrar(usuario);
            return ResponseEntity.ok(registrado);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error al registrar: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        try {
            Usuario encontrado = usuarioService.login(
                    usuario.getUsername(), usuario.getPassword());
            return ResponseEntity.ok(encontrado);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error en el login: " + e.getMessage());
        }
    }
}