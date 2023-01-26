package com.example.blog2.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.blog2.entity.UserEntity;

import java.util.Date;

/**
 * @author hikari
 * @version 1.0
 * @date 2021/7/6 16:39
 */
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;

public class TokenUtil {

    private static final long EXPIRE_TIME= 10 * 60 * 60 * 1000;

    //密钥盐
    private static final String TOKEN_SECRET = "QIUQIULQXIANGQUDACHANG";

    /**
     * 签名生成
     * @param user
     * @return
     */
    public static String sign(UserEntity user){
        String token = null;
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("userId", user.getId().toString())
                    .withClaim("userType", user.getType())
                    .withExpiresAt(expiresAt)
                    // 使用了HMAC256加密算法。
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));
        } catch (Exception e){
            e.printStackTrace();
        }
        return token;
    }

    /**
     * 签名验证
     * @param token
     * @return
     */
    public static boolean verify(String token){
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
            DecodedJWT jwt = verifier.verify(token);
            System.out.println("userId: " + jwt.getClaim("userId").asString());
            System.out.println("userType: " + jwt.getClaim("userType").asString());
            System.out.println("过期时间：      " + jwt.getExpiresAt());
            return true;
        } catch (Exception e){
            return false;
        }
    }

    /**
     * 管理员认证
     * @param token
     * @return
     */
    public static boolean adminVerify(String token){
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
            DecodedJWT jwt = verifier.verify(token);
            if ( "1".equals(jwt.getClaim("userType").asString())){
                return true;
            }
            return false;
        } catch (Exception e){
            return false;
        }
    }

}
