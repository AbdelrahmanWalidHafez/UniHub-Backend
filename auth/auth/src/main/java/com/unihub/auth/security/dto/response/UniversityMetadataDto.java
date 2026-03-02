package com.unihub.auth.security.dto.response;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniversityMetadataDto {

    private UUID uid;

    private UUID tid;

    private UUID cid;

    private Double gpa;
}
