package intern.nhhtuan.toeic_mentor.entity;

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
    private String rating; // "like", "dislike", or null
    private String feedback; // Optional text feedback
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
        metadata.put("feedback", feedback);
        metadata.put("ratedAt", ratedAt);
        return metadata;
    }

    @Override
    public MessageType getMessageType() {
        return originalMessage.getMessageType();
    }

    // Rating methods
    public void setRating(String rating, String feedback) {
        this.rating = rating;
        this.feedback = feedback;
        this.ratedAt = LocalDateTime.now();
    }
}
