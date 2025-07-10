package com.data.agricultural_material_management_system.utill;

import org.springframework.web.multipart.MultipartFile;

/**
 * 重置上传文件的文件名
 */
public class FileName {
    public static String getFileName(MultipartFile file) {
        //文件名
        String filename = file.getOriginalFilename();
        //获取最后一个.的位置
        int lastIndexOf = filename.lastIndexOf(".");
        //获取文件的后缀名
        String suffix = filename.substring(lastIndexOf);
        //上传到七牛的文件名
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1, 1); // Pass valid workerId and datacenterId
        return String.valueOf(idWorker.nextId()) + suffix;
    }
}
