package com.turf.common.util;

import com.turf.common.config.JwtConfig;
import com.turf.common.dto.ResultInfo;
import com.turf.common.dto.TokenInfo;
import com.turf.common.exception.AuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
public class JwtUtil {
    static final String USER_ID="USER_ID";
    static final String NAME="NAME";
    static final ResultInfo UNAUTHORIZED_TOKEN = new ResultInfo("403", "UNAUTHORIZED_TOKEN", "Unauthorized token", "F");

    @Autowired
    private JwtConfig jwtConfig;

    public TokenInfo generateTokens(String username, Integer userId, String name) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(USER_ID, userId);
        claims.put(NAME, name);
        return generateRefreshAndAccessToken(claims, username, userId, name);
    }

    public TokenInfo generateRefreshAndAccessToken(Map<String, Object> claims, String username, Integer userId, String name) {
        TokenInfo tokenInfo = new TokenInfo();
        Date accessExpiryTime = getAccessTokenExpiryTime();
        tokenInfo.setLoggedInUserId(userId);
        tokenInfo.setLoggedInUserName(username);
        tokenInfo.setAccessAllowed(true);
        tokenInfo.setName(name);
        tokenInfo.setAccessToken(generateAccessToken(claims, username));
        tokenInfo.setRefreshToken(generateRefreshToken(claims, username));
        tokenInfo.setTokenExpiry(accessExpiryTime);
        return tokenInfo;
    }

    private String generateAccessToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .claims(claims)
                .subject(userName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(getAccessTokenExpiryTime())
                .signWith(getSignKey(jwtConfig.getSecretKey()))
                .compact();
    }

    private String generateRefreshToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .claims(claims)
                .subject(userName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(getRefreshTokenExpiryTime())
                .signWith(getSignKey(jwtConfig.getSecretKey()))
                .compact();
    }

    private SecretKey getSignKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims getJWTPayload(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignKey(jwtConfig.getSecretKey()))
                    .build()
                    .parseSignedClaims(token).getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Jwt exception : {}", e.getMessage());
            throw new AuthenticationException(UNAUTHORIZED_TOKEN);
        }
    }

    private Date getAccessTokenExpiryTime() {
        Calendar currentTimeNow = Calendar.getInstance();
        currentTimeNow.add(Calendar.SECOND, jwtConfig.getAccessTokenExpTime());
        return currentTimeNow.getTime();
    }

    private Date getRefreshTokenExpiryTime() {
        Calendar currentTimeNow = Calendar.getInstance();
        currentTimeNow.add(Calendar.SECOND, jwtConfig.getRefreshTokenExpTime());
        return currentTimeNow.getTime();
    }

    public boolean isTokenValid(String token) {
        try {
            Claims tokenClaim = getJWTPayload(token);
            return tokenClaim != null;
        }
        catch (AuthenticationException ex){
            return false;
        }
    }

    public String extractUsername(String token) {
        Claims jwtPayload = getJWTPayload(token);
        return jwtPayload.getSubject();
    }

    public String extractUserId(String token) {
        Claims jwtPayload = getJWTPayload(token);
        return jwtPayload.get(USER_ID).toString();
    }

    public String extractName(String token) {
        Claims jwtPayload = getJWTPayload(token);
        return jwtPayload.get(NAME).toString();
    }
}
