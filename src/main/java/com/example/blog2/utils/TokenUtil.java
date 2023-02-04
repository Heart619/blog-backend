package com.example.blog2.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.blog2.entity.UserEntity;

import java.util.Date;

/**
 * @author mxp
 */
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.blog2.interceptor.TokenInterceptor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenUtil {

    private static final long EXPIRE_TIME = 2L * 60 * 60 * 1000;

    private static final long REFRESH_TOKEN_EXPIRE_TIME = 14L * 24 * 60 * 60 * 1000;

//    //密钥盐
//    private static final String TOKEN_SECRET = "QIUQIULEXIANGQUDACHANG";

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
                    .withClaim("userId", user.getId())
                    .withClaim("userType", user.getType())
                    .withExpiresAt(expiresAt)
                    // 使用了HMAC256加密算法。
                    .sign(Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey()));
        } catch (Exception e){
            e.printStackTrace();
        }
        return token;
    }

    public static String getRefreshToken(UserEntity user){
        String token = null;
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME);
            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("userId", user.getId())
                    .withClaim("userType", user.getType())
                    .withExpiresAt(expiresAt)
                    // 使用了HMAC256加密算法。
                    .sign(Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey()));
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
            JWTVerifier verifier = JWT
                    .require(Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey()))
                    .withIssuer("auth0")
                    .build();

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
    public static TokenInterceptor.UserTokenInfo adminVerify(String token) {
        try {
            JWTVerifier verifier = JWT
                    .require(Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey()))
                    .withIssuer("auth0")
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            if (jwt.getClaim("userType").asInt() > 0){
                TokenInterceptor.UserTokenInfo tokenInfo = new TokenInterceptor.UserTokenInfo();
                tokenInfo.setId(jwt.getClaim("userId").asLong());
                tokenInfo.setType(jwt.getClaim("userType").asInt());
                return tokenInfo;
            }
            return null;
        } catch (TokenExpiredException e){
            throw new TokenExpiredException("用户登陆过期，请重新登陆");
        } catch (Exception e) {
            throw new RuntimeException("非法用户");
        }
    }

    public static boolean checkCurUserStatus(Long id) {
        return TokenInterceptor.CUR_USER_INFO.get().getId().equals(id) || TokenInterceptor.CUR_USER_INFO.get().getType().equals(2);
    }

    public static boolean checkUserType() {
        return TokenInterceptor.CUR_USER_INFO.get().getType().compareTo(1) >= 0;
    }

    public static boolean checkRootType() {
        return TokenInterceptor.CUR_USER_INFO.get().getType().compareTo(1) > 0;
    }
}
