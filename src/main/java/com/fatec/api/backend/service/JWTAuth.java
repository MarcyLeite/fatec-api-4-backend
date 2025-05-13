package com.fatec.api.backend.service;

import com.fatec.api.backend.model.Usuario;
import com.fatec.api.backend.repository.UsuarioRepository;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JWTAuth {
    static String JWT_SECRET = "backless-server";
    static String JWT_ISSUER = "Backless";
    public Algorithm algorithm;
    public JWTVerifier verifier;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public JWTAuth() {
        algorithm = Algorithm.HMAC256(JWT_SECRET);
        verifier = JWT.require(algorithm).withIssuer(JWT_ISSUER).build();
    }

    public String createToken(Usuario user) {
        return JWT.create()
            .withIssuer(JWT_ISSUER)
            .withSubject("login")
            .withClaim("id", user.getId().toString())
            .withIssuedAt(new Date())
            .withJWTId(UUID.randomUUID().toString())
            .sign(algorithm);
    }
    public DecodedJWT decode(String token) {
        return verifier.verify(token);
    }
    public Long extractId (String token) {
        return Long.valueOf(decode(token).getClaim("id").asString());
    }

    public Usuario extractUser (String token) {
        Long tokenId = extractId(token);
        Optional<Usuario> optionalUser = usuarioRepository.findById(tokenId);
        if(!optionalUser.isPresent()) {
            return null;
        }
        return optionalUser.get();
    }
}