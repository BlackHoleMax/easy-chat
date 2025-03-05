package devblackholemax.easychattingroom.config;

import devblackholemax.easychattingroom.interceptors.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns("/chat/user/login", "/chat/user/register",
                        "/index.html",
                        "/register.html",
                        "/emoji.html",
                        "/css/**", "/js/**", "/lib/**");
    }
}