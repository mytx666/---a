package com.data.agricultural_material_management_system.service.impl;

import com.data.agricultural_material_management_system.common.Result;
import com.data.agricultural_material_management_system.entity.Image;
import com.data.agricultural_material_management_system.mapper.ImageMapper;
import com.data.agricultural_material_management_system.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageMapper imageMapper;

    @Override
    public List<Image> findAll() { return imageMapper.findAll(); }

    @Override
    public Image findById(Long id) { return imageMapper.findById(id); }

    @Override
    public void insert(Image image) { imageMapper.insert(image); }

    @Override
    public Result<Image> update(Image image) { imageMapper.update(image);
        return null;
    }

    @Override
    public void delete(Long id) { imageMapper.delete(id); }
} 