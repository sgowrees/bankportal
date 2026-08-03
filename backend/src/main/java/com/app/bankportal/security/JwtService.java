package com.app.bankportal.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;


@Component
public class JwtService {


    @Value("${jwt.secret}")
    private String secretKey;



    private SecretKey getSigningKey(){

        return Keys.hmacShaKeyFor(
                secretKey.getBytes()
        );
    }



    public String generateToken(Long userId, String username){


        return Jwts.builder()

                .subject(username)

                .claim("userId", userId)

                .issuedAt(new Date())

                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                + 1000 * 60 * 60
                        )
                )

                .signWith(getSigningKey())

                .compact();

    }



    public String extractUsername(String token){


        return Jwts.parser()

                .verifyWith(getSigningKey())

                .build()

                .parseSignedClaims(token)

                .getPayload()

                .getSubject();

    }



    public Long extractUserId(String token){


        return Jwts.parser()

                .verifyWith(getSigningKey())

                .build()

                .parseSignedClaims(token)

                .getPayload()

                .get("userId", Long.class);

    }

}