package com.justlife.casestudy.security;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

/**
 * JWT Utility class
 * Handles token generation/validation
 *
 * @author Mukesh
 */
@Component
@PropertySource("classpath:security.properties")
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.validityInMilliseconds}")
    private long expirationMs;

    public String generateToken(String username) {
        logger.info("Generating JWT for user: {}", username);

        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();

        } catch (Exception e) {
            logger.error("Error extracting username from token: {}", e.getMessage());
            throw e;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;

        } catch (ExpiredJwtException e) {
            logger.warn("JWT expired: {}", e.getMessage());

        } catch (UnsupportedJwtException e) {
            logger.warn("Unsupported JWT: {}", e.getMessage());

        } catch (MalformedJwtException e) {
            logger.warn("Malformed JWT: {}", e.getMessage());

        } catch (SignatureException e) {
            logger.warn("Invalid signature: {}", e.getMessage());

        } catch (IllegalArgumentException e) {
            logger.warn("Illegal JWT argument: {}", e.getMessage());
        }

        return false;
    }
}
