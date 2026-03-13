package com.unihub.ai.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatClient chatClient;

    private final ChatMemory chatMemory;

    private final ImageModel imageModel;

    private final OpenAiAudioTranscriptionModel transcriptionModel;

    @PostMapping(value="/message", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(@RequestHeader("X-User-Email") String email, @RequestParam("message")String message){
        return chatClient.prompt()
                .user(message)
                .system(promptSystemSpec -> promptSystemSpec.param("userEmail",email))
                .options(ToolCallingChatOptions.builder()
                        .internalToolExecutionEnabled(true)
                        .build())
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID,email))
                .stream()
                .content();
    }

    @PostMapping(value = "/voice", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatWithVoice(@RequestParam("message") MultipartFile voice ,@RequestHeader("X-User-Email") String email) {
        try {
            String originalFilename = voice.getOriginalFilename();
            String ext = (originalFilename != null && originalFilename.contains("."))
                    ? originalFilename.substring(originalFilename.lastIndexOf('.'))
                    : ".webm";

            Path tempFile = Files.createTempFile("voice_", ext);
            try {
                voice.transferTo(tempFile);
                Resource audioResource = new FileSystemResource(tempFile.toFile());

                OpenAiAudioTranscriptionOptions options = OpenAiAudioTranscriptionOptions.builder()
                        .model("whisper-1")
                        .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
                        .build();

                AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(audioResource, options);
                String transcribedText = transcriptionModel.call(prompt).getResult().getOutput();

                if (transcribedText.isBlank()) {
                    return Flux.just("Sorry, I couldn't understand the audio. Please try again.");
                }

                return chatClient.prompt()
                        .user(transcribedText)
                        .system(promptSystemSpec -> promptSystemSpec.param("userEmail",email))
                        .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID,email))
                        .options(ToolCallingChatOptions.builder()
                                .internalToolExecutionEnabled(true)
                                .build())
                        .stream()
                        .content();
            } finally {
                Files.deleteIfExists(tempFile);
            }
        } catch (Exception e) {
            return Flux.just("Error processing voice message: " + e.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<Message>> history(@RequestHeader("X-User-Email") String email){
        return ResponseEntity.ok(chatMemory.get(email));
    }

    @DeleteMapping("/history")
    public ResponseEntity<Void> deleteHistory(@RequestHeader("X-User-Email") String email){
        chatMemory.clear(email);
        return ResponseEntity.noContent().build();
    }


}
