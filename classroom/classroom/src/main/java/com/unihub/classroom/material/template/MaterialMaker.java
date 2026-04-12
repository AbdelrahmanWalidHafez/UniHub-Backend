package com.unihub.classroom.material.template;

import com.unihub.classroom.material.model.Material;
import com.unihub.classroom.material.service.IMaterialService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public abstract class MaterialMaker {

    private final IMaterialService materialService;

    public abstract void setMaterialType(Material material);

    public final Material generateMaterial(Material material, List<MultipartFile> materialFiles, UUID cid, HttpServletRequest request) throws IOException {
        return materialService.generateMaterial(material,materialFiles,cid,request);
    }
}
