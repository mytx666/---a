package com.data.agricultural_material_management_system.service;

import com.data.agricultural_material_management_system.common.Result;
import com.data.agricultural_material_management_system.entity.Image;
import java.util.List;

public interface ImageService {
    List<Image> findAll();
    Image findById(Long id);
    void insert(Image image);
    Result<Image> update(Image image);
    void delete(Long id);
} 