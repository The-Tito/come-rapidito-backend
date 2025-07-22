package org.alilopez.managers;

import io.javalin.http.ForbiddenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtTokenManagerImpl implements TokenManager {
    private static final String SECRET_KEY = "TediUMaTeiaTeNOmATAInERIerYbODlE";
    private final SecretKey key;

    public JwtTokenManagerImpl() {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    @Override
    public String issueToken(String nombre) {
        return Jwts.builder()
                .setSubject(nombre)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 horas
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean authorize(String token, String nombre) {

        try {
            // Validar inputs
            if (token == null || token.trim().isEmpty() ||
                    nombre == null || nombre.trim().isEmpty()) {
                return false;
            }

            // Remover Bearer si existe
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            Claims  claims = Jwts.parser()
                    .verifyWith(key)  // Cambió de setSigningKey a verifyWith
                    .build()
                    .parseSignedClaims(token)  // Cambió de parseClaimsJws a parseSignedClaims
                    .getPayload();

            String subject = claims.getSubject();

            if (subject == null) {
                return false;
            }

            // Comparar nombres
            boolean isValid = subject.trim().equalsIgnoreCase(nombre.trim());
            return isValid;

        } catch (Exception e) {

            return false;
        }
    }
}