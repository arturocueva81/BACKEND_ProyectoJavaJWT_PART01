package com.krakedev.jwt.controllers;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    
  
    @GetMapping("/perfil")
    public ResponseEntity<?> perfil(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401)
                        .body("Acceso denegado: token no proporcionado o formato incorrecto. "
                            + "Use: Bearer <TOKEN>");
            }
            String token = authHeader.substring(7);
            com.auth0.jwt.interfaces.DecodedJWT tokenDecodificado = JwtUtil.validarToken(token);
            String username = tokenDecodificado.getSubject();
            String rol = tokenDecodificado.getClaim("rol").asString();
            String saludo;
            if (rol.equals("ADMIN")) {
                saludo = 
                		"Bienvenido, Voluntario del Refugio " + username + ", tiene permisos habilitados.";
            } else {
                saludo = 
                		"Bienvenido, Adoptante " + username + ", Puedes ver las mascotas disponibles y actualizar tus datos.";
            }

            return ResponseEntity.ok(saludo);

        } catch (RuntimeException e) {

            return ResponseEntity.status(401)
                    .body("Acceso denegado: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error al acceder al perfil: " + e.getMessage());
        }
    }
}