package intern.nhhtuan.toeic_mentor.entity;

import lombok.Data;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;

import java.util.Map;

@Data
public class RatableMessage implements Message {
    private final Message originalMessage;
    private String messageId;
    private String conversationId;

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
        return metadata;
    }

    @Override
    public MessageType getMessageType() {
        return originalMessage.getMessageType();
    }
}
