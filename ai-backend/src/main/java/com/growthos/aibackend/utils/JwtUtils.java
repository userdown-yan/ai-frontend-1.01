package com.growthos.aibackend.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtils {

    // 密钥变长 + 符合安全规范（修复报错核心）
    private static final String SECRET = "growthos_1234567890_abcdefghijklmn"; // 足够长

    // 转成 Key 对象（新版 JJWT 要求）
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    // 7天过期
    private static final long EXPIRE = 1000L * 60 * 60 * 24 * 7;

    /**
     * 生成Token
     */
    public static String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 用 SECRET_KEY
                .compact();
    }

    /**
     * 兼容 Integer
     */
    public static String createToken(Integer userId) {
        return generateToken(userId.longValue());
    }

    /**
     * 解析Token
     */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY) // 这里也换成 SECRET_KEY
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取用户ID
     */
    public static Integer getUserId(String token) {
        try {
            Claims claims = parseToken(token);
            return Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            //  token 无效时返回 null
            return null;
        }
    }
}