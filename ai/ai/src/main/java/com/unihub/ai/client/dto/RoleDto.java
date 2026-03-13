package com.unihub.ai.client.dto;

import lombok.*;

import java.util.UUID;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

    private UUID rid;

    private String name;
}
