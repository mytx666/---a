package com.data.agricultural_material_management_system.mapper;

import com.data.agricultural_material_management_system.entity.Image;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ImageMapper {
    @Select("SELECT * FROM image")
    List<Image> findAll();

    @Select("SELECT * FROM image WHERE id = #{id}")
    Image findById(Long id);

    @Insert("INSERT INTO image(image_url, title) VALUES(#{imageUrl}, #{title})")
    int insert(Image image);

    @Update("UPDATE image SET image_url=#{imageUrl}, title=#{title} WHERE id=#{id}")
    int update(Image image);

    @Delete("DELETE FROM image WHERE id=#{id}")
    int delete(Long id);
} 
