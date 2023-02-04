package com.example.blog2.interceptor;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.blog2.utils.TokenUtil;
import com.example.blog2.vo.UserRecVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.JSONObject;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author mxp
 */

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    public static final ThreadLocal<UserTokenInfo> CUR_USER_INFO =  new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception{
        if("OPTIONS".equals(request.getMethod())){
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        response.setCharacterEncoding("UTF-8");
        String token = request.getHeader("token");
        if(!StringUtils.isEmpty(token)){
//        去掉前端返回的token前后的双引号
            token = token.substring(1, token.length() - 1);
            boolean result;
            try {
                UserTokenInfo userTokenInfo = TokenUtil.adminVerify(token);
                if (userTokenInfo == null) {
                    return false;
                }
                CUR_USER_INFO.set(userTokenInfo);
                return true;
            } catch (TokenExpiredException  e) {
                JSONObject json = new JSONObject();
                json.put("msg", e.getMessage());
                json.put("code", HttpStatus.UNAUTHORIZED.value());
                response.getWriter().append(json.toJSONString());
                return false;
            } catch (Exception e) {
                JSONObject json = new JSONObject();
                json.put("msg", e.getMessage());
                json.put("code", 444);
                response.getWriter().append(json.toJSONString());
                return false;
            }
        }

        response.setContentType("application/json; charset=utf-8");
        try{
            JSONObject json = new JSONObject();
            json.put("msg", "身份认证失败！");
            json.put("code", HttpStatus.UNAUTHORIZED.value());
            response.getWriter().append(json.toJSONString());
            log.error("认证失败，未通过拦截器, IP:{}", request.getRemoteAddr());
            return false;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        CUR_USER_INFO.remove();
    }

    @Data
    public static class UserTokenInfo {
        private Long id;
        private Integer type;
    }
}
