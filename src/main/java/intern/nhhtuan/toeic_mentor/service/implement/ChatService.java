package intern.nhhtuan.toeic_mentor.service.implement;

import lombok.Getter;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class ChatService {
    @Getter
    private final ChatClient chatClient;
    private final JdbcChatMemoryRepository jdbcChatMemoryRepository;

    @Value("classpath:/prompts/system-message.st")
    private Resource systemMessageResource;

    public ChatService(ChatClient.Builder builder, JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        this.jdbcChatMemoryRepository = jdbcChatMemoryRepository;
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .build();
        this.chatClient = builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    public Flux<String> getChatResponse(String message, String conversationId) {
        var systemMessage = new SystemMessage(systemMessageResource);
        var userMessage = new UserMessage(message);
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        return chatClient.prompt(prompt)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .stream()
                .content();
    }

    public Flux<String> getChatHistory(String conversationId) {
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .build();
        List<String> messages = chatMemory.get(conversationId).stream().map(Message::getText).toList();
        return Flux.fromIterable(messages);
    }
}
