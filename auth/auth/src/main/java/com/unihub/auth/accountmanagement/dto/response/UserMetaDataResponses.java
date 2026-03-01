package com.unihub.auth.accountmanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMetaDataResponses {
    @JsonProperty("users")
    List<UserMetaDataResponse> usersList;
}
