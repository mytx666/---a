package com.data.agricultural_material_management_system.config;

import com.data.agricultural_material_management_system.interceptor.PermissionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

/**
 * Web配置类
 * 用于注册拦截器等
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Autowired
    private PermissionInterceptor permissionInterceptor;

    /**
     * 跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(permissionInterceptor)
                .addPathPatterns("/**")  // 拦截所有请求
                .excludePathPatterns(    // 排除不需要权限检查的路径
                        "/user/login",   // 登录接口
                        "/user/register", // 注册接口
                        "/token/validate", // Token验证接口
                        "/token/refresh", // Token刷新接口
                        "/token/expiring", // Token过期检查接口
                        "/error",        // 错误页面
                        "/swagger-ui/**", // Swagger UI
                        "/v3/api-docs/**", // OpenAPI文档
                        "/favicon.ico"   // 网站图标
                );
    }
} 