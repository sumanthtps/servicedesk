package com.servicedesk.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.List;
import java.util.UUID;

@Service
public class JwtService {
    private final Key key = Keys.hmacShaKeyFor(
            "super-secret-256-bit-key-super-secret-256-bit-key".getBytes()
    );

    public JwtUser parse(String token) {
        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        Claims c = jws.getBody();
        UUID userId = UUID.fromString(c.get("uid", String.class));
        UUID orgId = UUID.fromString(c.get("org", String.class));
        String username = c.getSubject();
        return new JwtUser(userId, orgId, username, List.of());
    }
}
