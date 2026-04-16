package com.unihub.classroom.material.template.concrete;

import com.unihub.classroom.material.model.Material;
import com.unihub.classroom.material.model.enums.MaterialType;
import com.unihub.classroom.material.service.IMaterialService;
import com.unihub.classroom.material.template.MaterialMaker;

public class AnnouncementMaker extends MaterialMaker {

    public AnnouncementMaker(IMaterialService materialService) {
        super(materialService);
    }

    @Override
    public void setMaterialType(Material material) {
        material.setMaterialType(MaterialType.ANNOUNCEMENT);
    }
}
