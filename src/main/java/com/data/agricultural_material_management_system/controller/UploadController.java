package com.data.agricultural_material_management_system.controller;

import com.data.agricultural_material_management_system.common.Result;
import com.data.agricultural_material_management_system.utill.FileName;
import com.data.agricultural_material_management_system.utill.QiniuCloudUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/upload")
public class UploadController {

    /**
     * 上传图片到七牛云
     * @param image 图片文件
     * @return 图片URL
     */
    @PostMapping("/image")
    public Result<String> uploadImg(@RequestParam MultipartFile image, HttpServletRequest request) {
        if (image.isEmpty()) {
            return Result.buildResult(404, "上传文件不能为空", null);
        }
        try {
            byte[] bytes = image.getBytes();
            // 文件名
            String fileName = FileName.getFileName(image);
            // 上传工具类
            QiniuCloudUtil qiniuUtil = new QiniuCloudUtil();
            try {
                // 使用base64方式上传到七牛云
                String url = qiniuUtil.put64image(bytes, fileName);
                return Result.success(url);
            } catch (Exception e) {
                e.printStackTrace();
                return Result.buildResult(500, "文件上传发生异常: " + e.getMessage(), null);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.buildResult(500, "文件读取发生异常: " + e.getMessage(), null);
        }
    }
} 