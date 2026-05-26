package com.unihub.chat.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyTo {

    private String messageId;
    private String senderDisplayName;
    private String contentPreview;
}