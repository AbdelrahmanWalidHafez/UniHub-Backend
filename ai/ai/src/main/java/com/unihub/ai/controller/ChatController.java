package com.unihub.ai.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@Slf4j
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    @Value("classpath:/prompts/materialPromptTemplate.st")
    Resource systemPrompt;

    private final ChatClient chatClient;

    private final ChatMemory chatMemory;

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

    @PostMapping(value = "/voice",produces = MediaType.TEXT_EVENT_STREAM_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Flux<String> chatWithVoice(@RequestParam("message") MultipartFile voice, @RequestHeader("X-User-Email") String email) throws Exception {
        Path tempFile = Files.createTempFile("voice_", "_" + voice.getOriginalFilename());
        voice.transferTo(tempFile.toFile());
        System.out.println("File size on disk: " + Files.size(tempFile) + " bytes");
        Resource audioResource = new FileSystemResource(tempFile.toFile());
        var response = transcriptionModel.call(new AudioTranscriptionPrompt(audioResource,
                OpenAiAudioTranscriptionOptions.builder()
                        .language("en")
                        .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
                        .build()));
        log.info(response.getResult().getOutput());
        return chatClient.prompt()
                .user(response.getResult().getOutput())
                .system(promptSystemSpec -> promptSystemSpec.param("userEmail",email))
                .options(ToolCallingChatOptions.builder()
                        .internalToolExecutionEnabled(true)
                        .build())
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID,email))
                .stream()
                .content();
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

    @PostMapping(value = "/explain/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> explain(
            @RequestHeader("X-User-Email") String email,
            @PathVariable("id") String materialId) {

        return chatClient.prompt()
                .system(promptSystemSpec -> promptSystemSpec
                        .text(systemPrompt)
                        .param("userEmail", email)
                        .param("materialId", materialId))
                .options(ToolCallingChatOptions.builder()
                        .internalToolExecutionEnabled(true)
                        .build())
                .user("explain this material")
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, email))
                .stream()
                .content();
    }

}