package com.unihub.chat.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateGroupRequest {

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @NotEmpty
    @Size(max = 50)
    private List<@Email @NotBlank String> participantEmails;
}
