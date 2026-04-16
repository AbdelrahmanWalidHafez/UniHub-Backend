package com.unihub.classroom.material.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialResponsesDto {

    private List<MaterialResponseDto> materials;
}
