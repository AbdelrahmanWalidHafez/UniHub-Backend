package com.unihub.auth.accountmanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstructorUserRequest  extends  BaseUserRequest{

    @JsonProperty("college_id")
    @NotNull(message = "CID is required")
    private UUID cid;

}
