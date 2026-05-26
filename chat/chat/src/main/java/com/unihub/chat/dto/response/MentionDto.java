package com.unihub.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MentionDto {

    private String email;

    @JsonProperty("display_name")
    private String displayName;
}