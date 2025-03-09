package devblackholemax.easychattingroom.untils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = "your-secret-key"; // 替换为实际密钥
    private static final long EXPIRATION_MS = 1000 * 60 * 60 * 12; // 12小时过期

    // 生成 Token（基于用户 ID 和 Username）
    public static String generateToken(Long userId, String username) {
        return JWT.create()
                .withSubject(username)      // 存储用户名作为 Subject
                .withClaim("userId", userId)    // 自定义 Claim 存储用户 ID
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    // 解析 Token，返回用户名
    public static String parseUsername(String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                    .build()
                    .verify(token.replace("Bearer ", ""));
            return decodedJWT.getSubject();
        } catch (JWTVerificationException e) {
            throw new IllegalArgumentException("无效或过期的 Token");
        }
    }

    // 解析 Token，返回用户 ID
    public static String parseUserId(String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                    .build()
                    .verify(token.replace("Bearer ", ""));
            return decodedJWT.getClaim("userId").asString();
        } catch (JWTVerificationException e) {
            throw new IllegalArgumentException("无效或过期的 Token");
        }
    }
}
