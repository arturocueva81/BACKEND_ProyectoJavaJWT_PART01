package com.krakedev.jwt.controllers;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krakedev.jwt.entidades.Usuario;
import com.krakedev.jwt.services.UsuarioService;
import com.krakedev.jwt.utils.JwtUtil;

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
//            Usuario encontrado = usuarioService.login(
//                    usuario.getUsername(), usuario.getPassword());
//            return ResponseEntity.ok(encontrado);
        	
            Usuario encontrado = usuarioService.login(usuario.getUsername(), usuario.getPassword());
            String token = JwtUtil.generarToken(encontrado.getUsername(), encontrado.getRol());
            Map<String, Object> respuesta = new LinkedHashMap<>();
            
            respuesta.put("token", token);
            respuesta.put("username", encontrado.getUsername());
            respuesta.put("rol", encontrado.getRol());

            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
        	return ResponseEntity.status(401).body("Credenciales inválidas: " + e.getMessage());
        }
    }
}