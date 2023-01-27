package com.example.blog2.interceptor;

import com.example.blog2.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.JSONObject;

/**
 * @author mxp
 */

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception{
        if("OPTIONS".equals(request.getMethod())){
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        response.setCharacterEncoding("UTF-8");
        String token = request.getHeader("token");
//        去掉前端返回的token前后的双引号
        token = token.substring(1, token.length() - 1);
        if(!StringUtils.isEmpty(token)){
            boolean result = TokenUtil.adminVerify(token);
            if(result){
                return true;
            }
        }

        response.setContentType("application/json; charset=utf-8");
        try{
            JSONObject json = new JSONObject();
            json.put("msg","token verify fail");
            json.put("code","500");
            response.getWriter().append(json.toJSONString());
            log.warn("认证失败，未通过拦截器");
            return false;
        } catch (Exception e){
            response.sendError(500);
            return false;
        } finally {
            response.sendRedirect("/error");
        }
    }
}
