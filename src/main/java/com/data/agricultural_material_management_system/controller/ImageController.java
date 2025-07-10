package com.data.agricultural_material_management_system.controller;

import com.data.agricultural_material_management_system.common.Result;
import com.data.agricultural_material_management_system.entity.Image;
import com.data.agricultural_material_management_system.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/images")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @GetMapping
    public List<Image> list() { return imageService.findAll(); }

    @GetMapping("/{id}")
    public Image get(@PathVariable Long id) { return imageService.findById(id); }

    @PostMapping
    public String add(@RequestBody Image image) { imageService.insert(image);
    return "成功上传";
    }

    @PutMapping("/{id}")
    public Result<Image> update(@PathVariable Long id, @RequestBody Image image) {
        image.setId(id);
        imageService.update(image);
        Image image1 =imageService.findById(id);
        if(image1 == null){
            return Result.error("不存在");
        }
        return imageService.update(image);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        if(imageService.findById(id) == null){
            return "图片不存在";
        }
        imageService.delete(id);
    return "删除成功";}
} 