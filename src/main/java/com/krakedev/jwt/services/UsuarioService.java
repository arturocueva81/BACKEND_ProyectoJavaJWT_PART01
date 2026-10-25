package com.krakedev.jwt.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.krakedev.jwt.entidades.Usuario;
import com.krakedev.jwt.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

//    public Usuario registrar(Usuario usuario) {
//        try {
//            return usuarioRepository.save(usuario);
//        } catch (Exception e) {
//            throw new RuntimeException("Error al registrar: " + e.getMessage());
//        }
//    }
    
    public Usuario registrar(Usuario usuario) {
        try {
            String passwordHasheada = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt());
            usuario.setPassword(passwordHasheada);
            return usuarioRepository.save(usuario);
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar: " + e.getMessage());
        }
    }
    
    

//    public Usuario login(String username, String password) {
//        try {
//            Optional<Usuario> resultado = usuarioRepository.findByUsername(username);
//            if (resultado.isPresent()) {
//                Usuario usuario = resultado.get();
//
//                if (usuario.getPassword().equals(password)) {
//                    return usuario;
//                } else {
//                    throw new RuntimeException("Contraseña incorrecta");
//                }
//            } else {
//                throw new RuntimeException("Usuario no encontrado: " + username);
//            }
//        } catch (RuntimeException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new RuntimeException("Error en el login: " + e.getMessage());
//        }
//    }
    
    public Usuario login(String username, String password) {
        try {
            Optional<Usuario> resultado = usuarioRepository.findByUsername(username);
            if (resultado.isPresent()) {
                Usuario usuario = resultado.get();
                if (BCrypt.checkpw(password, usuario.getPassword())) {
                    return usuario;
                } else {
                    throw new RuntimeException("Contraseña incorrecta");
                }
            } else {
                throw new RuntimeException("Usuario no encontrado: " + username);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error en el login: " + e.getMessage());
        }
    }
}