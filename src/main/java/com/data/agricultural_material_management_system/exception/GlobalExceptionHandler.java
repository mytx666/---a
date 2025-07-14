package com.data.agricultural_material_management_system.exception;

import com.data.agricultural_material_management_system.common.Result;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.error("参数校验异常: {}", message);
        return Result.error(400, message);
    }
    
    /**
     * 处理绑定异常
     */
    @ExceptionHandler(BindException.class)
    public Result<String> handleBindException(BindException e) {
        String message = e.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.error("绑定异常: {}", message);
        return Result.error(400, message);
    }
    
    /**
     * 处理约束违反异常
     */
//    @ExceptionHandler(ConstraintViolationException.class)
//    public Result<String> handleConstraintViolationException(ConstraintViolationException e) {
//        String message = e.getConstraintViolations().stream()
//                .map(ConstraintViolation::getMessage)
//                .collect(Collectors.joining(", "));
//        log.error("约束违反异常: {}", message);
//        return Result.error(400, message);
//    }
    
    /**
     * 处理数据库唯一约束异常
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result<String> handleDuplicateKeyException(DuplicateKeyException e) {
        log.error("数据重复异常: {}", e.getMessage());
        String message = "数据已存在";
        
        // 根据具体的错误信息返回更友好的提示
        if (e.getMessage().contains("users.username")) {
            message = "用户名已存在";
        } else if (e.getMessage().contains("unit.unit_name")) {
            message = "单位名称已存在";
        } else if (e.getMessage().contains("materials.name")) {
            message = "物资名称已存在";
        } else if (e.getMessage().contains("suppliers.supplier_name")) {
            message = "供应商名称已存在";
        }
        
        return Result.error(400, message);
    }
    
    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<String> handleBusinessException(BusinessException e) {
        log.error("业务异常: {}", e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }
    
    /**
     * 处理权限拒绝异常
     */
    @ExceptionHandler(PermissionDeniedException.class)
    public Result<String> handlePermissionDeniedException(PermissionDeniedException e) {
        return Result.error(403, e.getMessage());
    }
    
    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<String> handleRuntimeException(RuntimeException e) {
        return Result.error(500, "系统错误：" + e.getMessage());
    }
    
    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        return Result.error(500, "未知错误：" + e.getMessage());
    }
} 