package com.unihub.subscription.inquries.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiresMetaData {
    @JsonProperty("inquires")
    List<InquiryMetaData> inquires;
}
