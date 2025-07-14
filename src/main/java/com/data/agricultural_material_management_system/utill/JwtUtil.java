package com.data.agricultural_material_management_system.utill;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    // Token expiration time (30 minutes)
    private static final long EXPIRE_TIME = 30 * 60 * 1000;
    // Secret key for signing
    private static final String TOKEN_SECRET = "yourSecretKey"; // Replace with a strong, private key

    /**
     * Creates a token with basic user info.
     *
     * @param userId   User ID
     * @param username Username
     * @return The generated token
     */
    public static String createToken(String userId, String username) {
        try {
            // Set expiration date
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            // Signing algorithm
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            // Set header information
            Map<String, Object> header = new HashMap<>(2);
            header.put("typ", "JWT");
            header.put("alg", "HS256");
            // Create and sign the token
            return JWT.create()
                    .withHeader(header)
                    .withClaim("userId", userId)
                    .withClaim("username", username)
                    .withIssuedAt(new Date())
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Creates a token with user roles and permissions.
     *
     * @param userId   User ID
     * @param username Username
     * @param roles    User roles
     * @param permissions User permissions
     * @return The generated token
     */
    public static String createTokenWithRoles(String userId, String username, String roles, String permissions) {
        try {
            // Set expiration date
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            // Signing algorithm
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            // Set header information
            Map<String, Object> header = new HashMap<>(2);
            header.put("typ", "JWT");
            header.put("alg", "HS256");
            // Create and sign the token
            return JWT.create()
                    .withHeader(header)
                    .withClaim("userId", userId)
                    .withClaim("username", username)
                    .withClaim("roles", roles)
                    .withClaim("permissions", permissions)
                    .withIssuedAt(new Date())
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Verifies a token.
     *
     * @param token The token to verify
     * @return True if the token is valid, false otherwise
     */
    public static DecodedJWT verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        } catch (JWTVerificationException exception) {
            // Invalid signature/claims
            return null;
        }
    }
    
    /**
     * Gets user ID from token.
     *
     * @param token The token
     * @return User ID as string, or null if token is invalid
     */
    public static String getUserIdFromToken(String token) {
        DecodedJWT jwt = verifyToken(token);
        if (jwt != null) {
            return jwt.getClaim("userId").asString();
        }
        return null;
    }
    
    /**
     * Gets username from token.
     *
     * @param token The token
     * @return Username, or null if token is invalid
     */
    public static String getUsernameFromToken(String token) {
        DecodedJWT jwt = verifyToken(token);
        if (jwt != null) {
            return jwt.getClaim("username").asString();
        }
        return null;
    }
    
    /**
     * Gets user roles from token.
     *
     * @param token The token
     * @return User roles as string, or null if token is invalid
     */
    public static String getRolesFromToken(String token) {
        DecodedJWT jwt = verifyToken(token);
        if (jwt != null) {
            return jwt.getClaim("roles").asString();
        }
        return null;
    }
    
    /**
     * Gets user permissions from token.
     *
     * @param token The token
     * @return User permissions as string, or null if token is invalid
     */
    public static String getPermissionsFromToken(String token) {
        DecodedJWT jwt = verifyToken(token);
        if (jwt != null) {
            return jwt.getClaim("permissions").asString();
        }
        return null;
    }
    
    /**
     * Checks if token is expired.
     *
     * @param token The token
     * @return True if token is expired, false otherwise
     */
    public static boolean isTokenExpired(String token) {
        DecodedJWT jwt = verifyToken(token);
        if (jwt != null) {
            return jwt.getExpiresAt().before(new Date());
        }
        return true;
    }
    
    /**
     * Gets token expiration time.
     *
     * @param token The token
     * @return Expiration date, or null if token is invalid
     */
    public static Date getTokenExpiration(String token) {
        DecodedJWT jwt = verifyToken(token);
        if (jwt != null) {
            return jwt.getExpiresAt();
        }
        return null;
    }
    
    /**
     * Refreshes a token (extends expiration time).
     *
     * @param token The original token
     * @return New token with extended expiration, or null if original token is invalid
     */
    public static String refreshToken(String token) {
        DecodedJWT jwt = verifyToken(token);
        if (jwt != null) {
            String userId = jwt.getClaim("userId").asString();
            String username = jwt.getClaim("username").asString();
            String roles = jwt.getClaim("roles").asString();
            String permissions = jwt.getClaim("permissions").asString();
            
            if (roles != null && permissions != null) {
                return createTokenWithRoles(userId, username, roles, permissions);
            } else {
                return createToken(userId, username);
            }
        }
        return null;
    }
} 