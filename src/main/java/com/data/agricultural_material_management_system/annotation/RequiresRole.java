package com.data.agricultural_material_management_system.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 角色控制注解
 * 用于标记需要特定角色的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRole {
    /**
     * 角色编码
     */
    String value();
    
    /**
     * 角色描述
     */
    String description() default "";
} 