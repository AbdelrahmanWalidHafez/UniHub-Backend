package com.unihub.subscription.subscriptionrequest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RoleDto {

    private UUID rid;

    private String name;
}