package com.unihub.ai.config;

import com.unihub.ai.tool.DocumentSearchTool;
import com.unihub.ai.tool.TimeTools;
import com.unihub.ai.tool.UserTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
@Configuration
public class ChatMemoryClientConfig {

    @Value("classpath:/prompts/systemPromptTemplate.st")
    Resource systemPrompt;

    @Bean
    public ChatClient chatClient(
            ChatClient.Builder builder
            , ChatMemory chatMemory
            , UserTools userTools
            , TimeTools timeTools
            , DocumentSearchTool documentSearchTool){

        return builder
                .defaultSystem(systemPrompt)
                .defaultOptions(ChatOptions.builder()
                        .model(OpenAiApi.ChatModel.GPT_4_O.getValue())
                        .maxTokens(500)
                        .temperature(.9)
                        .topP(.9)
                        .build())
                .defaultTools(userTools, timeTools, documentSearchTool)
                .defaultAdvisors(simpleLoggerAdvisor(), messageChatMemoryAdvisor(chatMemory)).build();
    }

    @Bean
    public ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        return MessageWindowChatMemory
                .builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(30)
                .build();
    }

    @Bean SimpleLoggerAdvisor simpleLoggerAdvisor(){
        return new SimpleLoggerAdvisor();
    }

    @Bean MessageChatMemoryAdvisor messageChatMemoryAdvisor(ChatMemory chatMemory){
        return MessageChatMemoryAdvisor.builder(chatMemory).build();
    }

}
