package uy.tse.periferico.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map; 

@Component
public class JwtTokenProvider {
    private final SecretKey key;
    private final long sessionExpirationMs;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.session.expiration-ms}") long sessionExpirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.sessionExpirationMs = sessionExpirationMs;
    }

     public String generateToken(String username, Long profesionalId, String tenantId, String rol) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + sessionExpirationMs);

        JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .claim("tenant_id", tenantId)
                .claim("rol", rol)
                .setIssuedAt(now)
                .setExpiration(expiryDate);

        if (profesionalId != null) {
            builder.claim("profesional_id", profesionalId);
        }

        return builder.signWith(key).compact();
    }

    public Claims validateAndGetClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
