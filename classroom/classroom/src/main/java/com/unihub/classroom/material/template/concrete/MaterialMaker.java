package com.unihub.classroom.material.template.concrete;

import com.unihub.classroom.material.model.Material;
import com.unihub.classroom.material.model.enums.MaterialType;
import com.unihub.classroom.material.service.IMaterialService;
import org.springframework.stereotype.Component;

@Component
public class MaterialMaker extends com.unihub.classroom.material.template.MaterialMaker {

    private IMaterialService materialService;

    public MaterialMaker(IMaterialService materialService) {
        super(materialService);
    }

    @Override
    public void setMaterialType(Material material) {
        material.setMaterialType(MaterialType.MATERIAL);
    }
}