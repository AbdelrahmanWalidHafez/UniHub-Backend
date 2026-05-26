package com.unihub.chat.model;

import com.unihub.chat.model.embedded.LastMessage;
import com.unihub.chat.model.embedded.Participant;
import com.unihub.chat.model.enums.ChatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_rooms")
public class ChatRoom {

    @Id
    private String id;

    private ChatType type;

    private String name;

    @Indexed
    private UUID tid;

    @Builder.Default
    private List<Participant> participants = new ArrayList<>();

    private LastMessage lastMessage;

    private String createdBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}