package com.data.agricultural_material_management_system.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限控制注解
 * 用于标记需要权限控制的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermission {
    /**
     * 权限编码
     */
    String value();
    
    /**
     * 权限描述
     */
    String description() default "";
} 