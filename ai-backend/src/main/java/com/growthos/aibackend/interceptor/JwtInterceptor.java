package com.growthos.aibackend.interceptor;

import com.growthos.aibackend.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // 放行 OPTIONS
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty()) {

            response.setContentType("application/json;charset=UTF-8");

            response.getWriter().write("""
                    {
                      "code":401,
                      "message":"未登录",
                      "data":null
                    }
                    """);

            return false;
        }

        try {

            Claims claims = JwtUtils.parseToken(token);

            request.setAttribute(
                    "userId",
                    claims.getSubject()
            );

            return true;

        } catch (Exception e) {

            response.setContentType("application/json;charset=UTF-8");

            response.getWriter().write("""
                    {
                      "code":401,
                      "message":"token无效",
                      "data":null
                    }
                    """);

            return false;
        }
    }
}