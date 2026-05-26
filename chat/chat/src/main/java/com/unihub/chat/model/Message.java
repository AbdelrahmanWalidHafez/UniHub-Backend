package com.unihub.chat.model;

import com.unihub.chat.model.embedded.Mention;
import com.unihub.chat.model.embedded.ReplyTo;
import com.unihub.chat.model.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "messages")
public class Message {

    @Id
    private String id;

    @Indexed
    private String roomId;

    private String senderEmail;

    private String senderDisplayName;

    private MessageType type;

    private String content;

    private String imageUrl;

    private byte[] voiceData;

    private Integer voiceDurationSecs;

    @Builder.Default
    private List<Mention> mentions = new ArrayList<>();

    private ReplyTo replyTo;

    @Builder.Default
    private List<String> readBy = new ArrayList<>();

    private LocalDateTime editedAt;

    private LocalDateTime deletedAt;

    @CreatedDate
    private LocalDateTime createdAt;
}