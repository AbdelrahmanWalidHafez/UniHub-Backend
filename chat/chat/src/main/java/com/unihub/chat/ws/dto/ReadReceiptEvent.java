package com.unihub.chat.ws.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReadReceiptEvent {

    private String roomId;
    private String readerEmail;
}
