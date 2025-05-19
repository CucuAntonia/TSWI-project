//package com.zegasoftware.stock_management.jwt;
//
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.stereotype.Component;
//import com.zegasoftware.stock_management.model.dto.user.UserDetails;
//import org.springframework.beans.factory.annotation.Value;
//
//
//import java.util.Date;
//
//@Component
//public class JwtUtil {
//    @Value("${jwt.secret}")
//    private String secretKey;
//    @Value("${jwt.expiration}")
//    private long expirationMs;
//
//    public String generateToken(UserDetails user) {
//        Date now = new Date();
//        return Jwts.builder()
//                .setSubject(user.getUsername())
//                .claim("roles", user.getAuthorities()
//                        .stream()
//                        .map(GrantedAuthority::getAuthority)
//                        .toList())
//                .setIssuedAt(now)
//                .setExpiration(new Date(now.getTime() + expirationMs))
//                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parser()
//                    .setSigningKey(secretKey.getBytes())
//                    .build()
//                    .parseClaimsJws(token);
//            return true;
//        } catch (JwtException e) {
//            return false;
//        }
//    }
//
//    public String getUsername(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(secretKey.getBytes())
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//}
