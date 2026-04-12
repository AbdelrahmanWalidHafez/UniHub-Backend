package com.unihub.classroom.material.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaterialDto {

    @JsonProperty("head_line")
    @NotBlank(message = "Headline must not be empty")
    @Size(min = 3, max = 100, message = "Headline must be between 3 and 100 characters")
    private String headLine;

    @NotBlank(message = "Description must not be empty")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
}
