package com.data.agricultural_material_management_system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.data.agricultural_material_management_system.mapper")
public class AgriculturalMaterialManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgriculturalMaterialManagementSystemApplication.class, args);
    }

}
