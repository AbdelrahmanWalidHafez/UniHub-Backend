package com.unihub.chat.ws.dto;

import com.unihub.chat.dto.response.MessageDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomUpdateEvent {

    private String type;
    private String roomId;
    private MessageDto message;
}
