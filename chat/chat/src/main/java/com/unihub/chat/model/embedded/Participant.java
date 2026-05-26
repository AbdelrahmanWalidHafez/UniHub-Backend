package com.unihub.chat.model.embedded;

import com.unihub.chat.model.enums.ParticipantRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Participant {

    private String email;
    private String displayName;
    private ParticipantRole role;
    private LocalDateTime lastSeenAt;
    private String lastReadMessageId;
}