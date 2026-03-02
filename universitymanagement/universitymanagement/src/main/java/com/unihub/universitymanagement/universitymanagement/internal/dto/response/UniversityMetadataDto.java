package com.unihub.universitymanagement.universitymanagement.internal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UniversityMetadataDto {
    private UUID uid;

    private UUID tid;

    private UUID cid;
}
