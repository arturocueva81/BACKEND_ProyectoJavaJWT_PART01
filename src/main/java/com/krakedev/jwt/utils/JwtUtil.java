package com.krakedev.jwt.utils;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtUtil {

    private static final String CLAVE_SECRETA = "PatitasAlRescate_ClaveSecreta_2026";
    private static final long TIEMPO_EXPIRACION = 30 * 60 * 1000;
    private static final Algorithm ALGORITMO = Algorithm.HMAC256(CLAVE_SECRETA);
    public static String generarToken(String username, String rol) {
        Date fechaActual = new Date();
        Date fechaExpiracion = new Date(fechaActual.getTime() + TIEMPO_EXPIRACION);

        String token = JWT
        		.create()
        		.withSubject(username)
        		.withClaim("rol", rol)
        		.withIssuedAt(fechaActual)
        		.withExpiresAt(fechaExpiracion)
        		.sign(ALGORITMO);               
        return token;
    }

    public static DecodedJWT validarToken(String token) {
        try {
            DecodedJWT tokenDecodificado = JWT
            		.require(ALGORITMO)
                    .build()
                    .verify(token);
            return tokenDecodificado;
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Token inválido o expirado: " + e.getMessage());
        }
    }
}