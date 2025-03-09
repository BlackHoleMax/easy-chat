package devblackholemax.easychattingroom.interceptors;

import devblackholemax.easychattingroom.untils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {
        // 1. 获取 Authorization 请求头
        String authHeader = request.getHeader("Authorization");

        // 2. 验证 Token 格式
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            return false;
        }

        // 3. 提取并解析 Token
        String token = authHeader.substring(7); // 移除 "Bearer " 前缀
        try {
            String username = JwtUtil.parseUsername(token); // 直接解析用户名
            return true;
        } catch (IllegalArgumentException e) { // 捕获 Token 无效或过期异常
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            return false;
        }
    }
}