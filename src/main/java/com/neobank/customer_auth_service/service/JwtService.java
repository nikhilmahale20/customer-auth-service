package com.neobank.customer_auth_service.service;

import com.neobank.customer_auth_service.model.Credentials;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;

import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey;

    private final long expiration;

    public JwtService(
            @Value("${jwt.secret}")
            String secret,

            @Value("${jwt.expiration}")
            long expiration
    ) {

        this.secretKey =
                Keys.hmacShaKeyFor(
                        secret.getBytes(
                                StandardCharsets.UTF_8
                        )
                );

        this.expiration =
                expiration;
    }

    public String generateToken(
            Credentials credentials
    ) {

        Date issuedAt =
                new Date();

        Date expirationDate =
                new Date(
                        issuedAt.getTime()
                                + expiration
                );

        return Jwts.builder()
                .subject(
                        credentials.getUsername()
                )
                .claim(
                        "customerId",
                        credentials.getCustomerId()
                )
                .claim(
                        "role",
                        credentials
                                .getRole()
                                .name()
                )
                .issuedAt(
                        issuedAt
                )
                .expiration(
                        expirationDate
                )
                .signWith(
                        secretKey
                )
                .compact();
    }

    public String extractUsername(
            String token
    ) {

        return extractAllClaims(
                token
        ).getSubject();
    }

    public String extractCustomerId(
            String token
    ) {

        return extractAllClaims(
                token
        ).get(
                "customerId",
                String.class
        );
    }

    public String extractRole(
            String token
    ) {

        return extractAllClaims(
                token
        ).get(
                "role",
                String.class
        );
    }

    public boolean isTokenValid(
            String token
    ) {

        try {

            Claims claims =
                    extractAllClaims(
                            token
                    );

            return claims
                    .getExpiration()
                    .after(
                            new Date()
                    );

        } catch (Exception exception) {

            return false;
        }
    }

    private Claims extractAllClaims(
            String token
    ) {

        return Jwts.parser()
                .verifyWith(
                        secretKey
                )
                .build()
                .parseSignedClaims(
                        token
                )
                .getPayload();
    }
}