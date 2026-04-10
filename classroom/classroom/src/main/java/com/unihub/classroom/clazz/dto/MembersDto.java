package com.unihub.classroom.clazz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembersDto {

    @JsonProperty
    private List<MemberDto> members;
}
