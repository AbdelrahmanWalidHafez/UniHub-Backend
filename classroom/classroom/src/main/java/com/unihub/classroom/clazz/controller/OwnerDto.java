package com.unihub.classroom.clazz.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerDto {

    @JsonProperty("owner_email")
    private String email;

}
