package intern.nhhtuan.toeic_mentor.advisor;

import intern.nhhtuan.toeic_mentor.entity.RatableMessage;
import intern.nhhtuan.toeic_mentor.repository.RatingEnabledChatMemoryRepository;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.prompt.Prompt;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

        List<Message> transformedMessages = modifiedRequest.prompt().getInstructions().stream()
                .map(msg -> {
                    if (MessageType.ASSISTANT.equals(msg.getMessageType())) {
                        return new AssistantMessage(msg.getText(), msg.getMetadata());
                    }

                    if (MessageType.USER.equals(msg.getMessageType())) {
                        return UserMessage.builder()
                                .text(msg.getText())
                                .metadata(msg.getMetadata())
                                .build();
                    }

                    if (MessageType.SYSTEM.equals(msg.getMessageType())) {
                        return SystemMessage.builder()
                                .text(msg.getText())
                                .metadata(msg.getMetadata())
                                .build();
                    }

                    return msg;
                }).toList();

        modifiedRequest = ChatClientRequest.builder()
                .prompt(Prompt.builder().messages(transformedMessages).build())
                .context(modifiedRequest.context())
                .build();

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
        List<Message> transformedMessages = modifiedRequest.prompt().getInstructions().stream()
                .map(msg -> {
                    if (MessageType.ASSISTANT.equals(msg.getMessageType())) {
                        return new AssistantMessage(msg.getText(), msg.getMetadata());
                    }

                    if (MessageType.USER.equals(msg.getMessageType())) {
                        return UserMessage.builder()
                                .text(msg.getText())
                                .metadata(msg.getMetadata())
                                .build();
                    }

                    if (MessageType.SYSTEM.equals(msg.getMessageType())) {
                        return SystemMessage.builder()
                                .text(msg.getText())
                                .metadata(msg.getMetadata())
                                .build();
                    }

                    return msg;
                }).toList();

        modifiedRequest = ChatClientRequest.builder()
                .prompt(Prompt.builder().messages(transformedMessages).build())
                .context(modifiedRequest.context())
                .build();

        // Execute streaming call and collect final response
        return streamAdvisorChain.nextStream(modifiedRequest)
                .collectList()
                .doOnNext(responses -> {
                    if (!responses.isEmpty()) {
                        ChatClientResponse finalResponse = responses.getFirst();
                        storeAssistantResponse(responses, conversationId);
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
        updatedMessages.addAll(request.prompt().getInstructions());

        return request.mutate()
                .prompt(request.prompt().mutate().messages(updatedMessages).build())
                .build();
    }

    private void storeUserMessage(ChatClientRequest request, String conversationId) {
        if (request.prompt().getUserMessage().getText() != null && !request.prompt().getUserMessage().getText().isEmpty()) {
            UserMessage userMessage = new UserMessage(request.prompt().getUserMessage().getText());
            String messageId = UUID.randomUUID().toString();
            RatableMessage ratableUserMessage = new RatableMessage(userMessage, messageId, conversationId);
            repository.saveAll(conversationId, List.of(ratableUserMessage));
        }
    }

    private void storeAssistantResponse(List<ChatClientResponse> responses, String conversationId) {
        StringBuilder message = new StringBuilder();
        for (ChatClientResponse response : responses) {
            if (response.chatResponse() != null && response.chatResponse().getResult() != null &&
                    response.chatResponse().getResult().getOutput() != null) {
                message.append(response.chatResponse().getResult().getOutput().getText());
            }
        }
//        if (response.chatResponse().getResult() != null && response.chatResponse().getResult().getOutput() != null) {
        Message assistantMessage = new AssistantMessage(message.toString(), responses.getFirst().chatResponse().getResult().getOutput().getMetadata());
//            Message message = new AssistantMessage()
        String messageId = UUID.randomUUID().toString();
        RatableMessage ratableAssistantMessage = new RatableMessage(assistantMessage, messageId, conversationId);
        repository.saveAll(conversationId, List.of(ratableAssistantMessage));
//        }
    }

    private void storeAssistantResponse(ChatClientResponse response, String conversationId) {
        if (response.chatResponse().getResult() != null && response.chatResponse().getResult().getOutput() != null) {
            Message assistantMessage = (Message) response.chatResponse().getResult().getOutput();
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
        Object conversationId = request.context().get(ChatMemory.CONVERSATION_ID);
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
