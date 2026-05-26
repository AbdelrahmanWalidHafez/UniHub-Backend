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
public class AddParticipantsRequest {

    @NotEmpty
    @Size(max = 50)
    private List<@Email @NotBlank String> emails;
}
