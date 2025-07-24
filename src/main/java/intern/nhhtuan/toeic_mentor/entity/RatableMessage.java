package intern.nhhtuan.toeic_mentor.entity;

import intern.nhhtuan.toeic_mentor.entity.enums.EChatMemoryRating;
import lombok.Data;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class RatableMessage implements Message {
    private final Message originalMessage;
    private String messageId;
    private String conversationId;
    private EChatMemoryRating rating; // "like", "dislike", or null
    private LocalDateTime ratedAt;

    public RatableMessage(Message originalMessage, String messageId, String conversationId) {
        this.originalMessage = originalMessage;
        this.messageId = messageId;
        this.conversationId = conversationId;
    }

    // Delegate to original message
    @Override
    public String getText() {
        return originalMessage.getText();
    }

    @Override
    public Map<String, Object> getMetadata() {
        Map<String, Object> metadata = originalMessage.getMetadata();
        metadata.put("messageId", messageId);
        metadata.put("conversationId", conversationId);
        metadata.put("rating", rating);
        metadata.put("ratedAt", ratedAt);
        return metadata;
    }

    @Override
    public MessageType getMessageType() {
        return originalMessage.getMessageType();
    }

    // Rating methods
    public void setRating(EChatMemoryRating rating) {
        this.rating = rating;
        this.ratedAt = LocalDateTime.now();
    }
}
