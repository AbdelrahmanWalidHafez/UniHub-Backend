package com.unihub.chat.controller;

import com.unihub.chat.common.util.HttpHeadersUtils;
import com.unihub.chat.dto.request.EditMessageRequest;
import com.unihub.chat.dto.response.MessageDto;
import com.unihub.chat.model.Message;
import com.unihub.chat.service.IMessageService;
import com.unihub.chat.ws.mapper.MessageWsMapper;
import com.unihub.chat.ws.publisher.ChatMessageWsPublisher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MessageController {

    private final IMessageService messageService;
    private final HttpHeadersUtils headersUtils;
    private final MessageWsMapper messageWsMapper;
    private final ChatMessageWsPublisher messageWsPublisher;

    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<Page<MessageDto>> getMessages(@PathVariable String roomId,
                                                         @RequestParam(value = "page", defaultValue = "1") int page,
                                                         HttpServletRequest httpRequest) {
        String email = headersUtils.extractEmail(httpRequest);
        Page<Message> messages = messageService.getMessages(roomId, email, page);
        return ResponseEntity.ok(messages.map(messageWsMapper::toDto));
    }

    @PostMapping(value = "/rooms/{roomId}/messages/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageDto> sendImage(@PathVariable String roomId,
                                                 @RequestPart("file") MultipartFile image,
                                                 @RequestPart(value = "replyTo", required = false) String replyToMessageId,
                                                 @RequestPart(value = "content", required = false) String content,
                                                 HttpServletRequest httpRequest) throws IOException {
        String email = headersUtils.extractEmail(httpRequest);
        Message message = messageService.sendImageMessage(roomId, email, image, replyToMessageId, content);
        messageWsPublisher.publishMessage(message);
        return ResponseEntity.ok(messageWsMapper.toDto(message));
    }

    @PostMapping(value = "/rooms/{roomId}/messages/voice", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageDto> sendVoice(@PathVariable String roomId,
                                                 @RequestPart("file") MultipartFile voice,
                                                 @RequestParam(value = "duration_secs", required = false) Integer durationSecs,
                                                 @RequestPart(value = "replyTo", required = false) String replyToMessageId,
                                                 HttpServletRequest httpRequest) throws IOException {
        String email = headersUtils.extractEmail(httpRequest);
        Message message = messageService.sendVoiceMessage(roomId, email, voice, durationSecs, replyToMessageId);
        messageWsPublisher.publishMessage(message);
        return ResponseEntity.ok(messageWsMapper.toDto(message));
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<MessageDto> editMessage(@PathVariable String messageId,
                                                   @RequestBody @Valid EditMessageRequest request,
                                                   HttpServletRequest httpRequest) {
        String email = headersUtils.extractEmail(httpRequest);
        Message updated = messageService.editMessage(messageId, email, request.getContent());
        messageWsPublisher.publishMessage(updated);
        return ResponseEntity.ok(messageWsMapper.toDto(updated));
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<MessageDto> deleteMessage(@PathVariable String messageId,
                                                     HttpServletRequest httpRequest) {
        String email = headersUtils.extractEmail(httpRequest);
        Message deleted = messageService.deleteMessage(messageId, email);
        messageWsPublisher.publishMessage(deleted);
        return ResponseEntity.ok(messageWsMapper.toDto(deleted));
    }

    @GetMapping("/messages/{messageId}/voice")
    public ResponseEntity<byte[]> getVoice(@PathVariable String messageId,
                                            HttpServletRequest httpRequest) {
        String email = headersUtils.extractEmail(httpRequest);
        byte[] audio = messageService.getVoiceMessage(messageId, email).getVoiceData();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"voice-" + messageId + ".ogg\"")
                .contentType(MediaType.parseMediaType("audio/ogg"))
                .body(audio);
    }
}
