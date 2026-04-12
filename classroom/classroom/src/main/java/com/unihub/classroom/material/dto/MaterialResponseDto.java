package com.unihub.classroom.material.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unihub.classroom.material.model.enums.MaterialType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaterialResponseDto {

    @JsonProperty("material_id")
    private UUID mid;

    @JsonProperty("head_line")
    private String headLine;

    private String description;

    @JsonProperty("material_type")
    private MaterialType materialType;

    @JsonProperty("material_urls")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> materialUrls;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("updated_by")
    private String updatedBy;

}
