package intern.nhhtuan.toeic_mentor.advisor;

import intern.nhhtuan.toeic_mentor.entity.RatableMessage;
import intern.nhhtuan.toeic_mentor.repository.RatingEnabledChatMemoryRepository;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.MessageType;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.stream.Collectors;

public class RatingEnabledChatMemoryAdvisor implements CallAdvisor, StreamAdvisor {

    private final RatingEnabledChatMemoryRepository repository;
    private final int chatMemoryRetrieveSize;
    private final String defaultConversationId;
    private final int order;

    public RatingEnabledChatMemoryAdvisor(RatingEnabledChatMemoryRepository repository) {
        this(repository, 20, "default", 0);
    }

    public RatingEnabledChatMemoryAdvisor(RatingEnabledChatMemoryRepository repository,
                                          int chatMemoryRetrieveSize,
                                          String defaultConversationId,
                                          int order) {
        this.repository = repository;
        this.chatMemoryRetrieveSize = chatMemoryRetrieveSize;
        this.defaultConversationId = defaultConversationId;
        this.order = order;
    }

    @Override
    public String getName() {
        return "RatingEnabledChatMemoryAdvisor";
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        String conversationId = getConversationId(chatClientRequest);

        // Add conversation history to request
        ChatClientRequest modifiedRequest = addConversationHistory(chatClientRequest, conversationId);

        // Store user message
        storeUserMessage(chatClientRequest, conversationId);

        // Execute the call
        ChatClientResponse response = callAdvisorChain.nextCall(modifiedRequest);

        // Store assistant response
        storeAssistantResponse(response, conversationId);

        return response;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        String conversationId = getConversationId(chatClientRequest);

        // Add conversation history to request
        ChatClientRequest modifiedRequest = addConversationHistory(chatClientRequest, conversationId);

        // Store user message
        storeUserMessage(chatClientRequest, conversationId);

        // Execute streaming call and collect final response
        return streamAdvisorChain.nextStream(modifiedRequest)
                .collectList()
                .doOnNext(responses -> {
                    if (!responses.isEmpty()) {
                        ChatClientResponse finalResponse = responses.get(responses.size() - 1);
                        storeAssistantResponse(finalResponse, conversationId);
                    }
                })
                .flatMapMany(Flux::fromIterable);
    }

    private ChatClientRequest addConversationHistory(ChatClientRequest request, String conversationId) {
        // Get conversation history
        List<Message> allMessages = repository.findByConversationId(conversationId);
        List<Message> memoryMessages = getRecentMessages(allMessages, chatMemoryRetrieveSize);

        if (memoryMessages.isEmpty()) {
            return request;
        }

        // Add history to existing messages
        List<Message> updatedMessages = new ArrayList<>();
        updatedMessages.addAll(memoryMessages);
        updatedMessages.addAll(request.messages());

        return ChatClientRequest.from(request)
                .messages(updatedMessages)
                .build();
    }

    private void storeUserMessage(ChatClientRequest request, String conversationId) {
        if (request.userText() != null && !request.userText().isEmpty()) {
            UserMessage userMessage = new UserMessage(request.userText());
            String messageId = UUID.randomUUID().toString();
            RatableMessage ratableUserMessage = new RatableMessage(userMessage, messageId, conversationId);
            repository.saveAll(conversationId, List.of(ratableUserMessage));
        }
    }

    private void storeAssistantResponse(ChatClientResponse response, String conversationId) {
        if (response.getResult() != null && response.getResult().getOutput() != null) {
            Message assistantMessage = (Message) response.getResult().getOutput();
            String messageId = UUID.randomUUID().toString();
            RatableMessage ratableAssistantMessage = new RatableMessage(assistantMessage, messageId, conversationId);
            repository.saveAll(conversationId, List.of(ratableAssistantMessage));
        }
    }

    private List<Message> getRecentMessages(List<Message> allMessages, int limit) {
        if (allMessages.size() <= limit) {
            return allMessages;
        }
        return allMessages.subList(allMessages.size() - limit, allMessages.size());
    }

    private String getConversationId(ChatClientRequest request) {
        // Check request parameters for conversation ID
        Object conversationId = request.advisorParams().get(ChatMemory.CONVERSATION_ID);
        if (conversationId instanceof String) {
            return (String) conversationId;
        }
        return defaultConversationId;
    }

    public static Builder builder(RatingEnabledChatMemoryRepository repository) {
        return new Builder(repository);
    }

    public static class Builder {
        private final RatingEnabledChatMemoryRepository repository;
        private int chatMemoryRetrieveSize = 20;
        private String defaultConversationId = "default";
        private int order = 0;

        public Builder(RatingEnabledChatMemoryRepository repository) {
            this.repository = repository;
        }

        public Builder chatMemoryRetrieveSize(int size) {
            this.chatMemoryRetrieveSize = size;
            return this;
        }

        public Builder defaultConversationId(String conversationId) {
            this.defaultConversationId = conversationId;
            return this;
        }

        public Builder order(int order) {
            this.order = order;
            return this;
        }

        public RatingEnabledChatMemoryAdvisor build() {
            return new RatingEnabledChatMemoryAdvisor(
                    repository, chatMemoryRetrieveSize, defaultConversationId, order
            );
        }
    }
}
