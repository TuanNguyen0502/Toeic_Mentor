package intern.nhhtuan.toeic_mentor.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import intern.nhhtuan.toeic_mentor.dto.response.ChatbotResponse;
import intern.nhhtuan.toeic_mentor.entity.RatableMessage;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class RatingEnabledChatMemoryRepository implements ChatMemoryRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public RatingEnabledChatMemoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public String getCreatedAtByMessageId(String messageId) {
        return jdbcTemplate.queryForObject("""
                SELECT created_at FROM spring_ai_chat_memory_enhanced 
                WHERE id = ?
                """, String.class, messageId);
    }

    public String getConversationIdByMessageId(String messageId) {
        return jdbcTemplate.queryForObject("""
                SELECT conversation_id FROM spring_ai_chat_memory_enhanced 
                WHERE id = ?
                """, String.class, messageId);
    }

    public boolean renameConversationId(String oldConversationId, String newConversationId) {
        int updatedRows = jdbcTemplate.update("""
                UPDATE spring_ai_chat_memory_enhanced 
                SET conversation_id = ? 
                WHERE conversation_id = ?
                """, newConversationId, oldConversationId);
        return updatedRows > 0;
    }

    public boolean existsByMessageId(String messageId) {
        Integer count = jdbcTemplate.queryForObject("""
                SELECT COUNT(*) FROM spring_ai_chat_memory_enhanced 
                WHERE id = ?
                """, Integer.class, messageId);
        return count != null && count > 0;
    }

    public boolean existsByConversationId(String conversationId) {
        Integer count = jdbcTemplate.queryForObject("""
                SELECT COUNT(*) FROM spring_ai_chat_memory_enhanced 
                WHERE conversation_id = ?
                """, Integer.class, conversationId);
        return count != null && count > 0;
    }

    public List<ChatbotResponse> getChatHistory(String conversationId) {
        return jdbcTemplate.query("""
                        SELECT id, content, conversation_id, message_type 
                        FROM spring_ai_chat_memory_enhanced 
                        WHERE conversation_id = ? 
                        ORDER BY created_at ASC
                        """,
                (rs, rowNum) -> {
                    ChatbotResponse response = new ChatbotResponse();
                    response.setMessageId(rs.getString("id"));
                    response.setContent(rs.getString("content"));
                    response.setConversationId(rs.getString("conversation_id"));
                    response.setMessageType(rs.getString("message_type"));
                    return response;
                },
                conversationId
        );
    }

    @Override
    public List<String> findConversationIds() {
        return jdbcTemplate.queryForList("""
                SELECT DISTINCT conversation_id 
                FROM spring_ai_chat_memory_enhanced 
                ORDER BY conversation_id
                """, String.class);
    }

    @Override
    public List<Message> findByConversationId(String conversationId) {
        return jdbcTemplate.query("""
                        SELECT * FROM spring_ai_chat_memory_enhanced 
                        WHERE conversation_id = ? 
                        ORDER BY created_at ASC
                        """,
                new RatableMessageRowMapper(),
                conversationId
        );
    }

    @Override
    public void saveAll(String conversationId, List<Message> messages) {
        for (Message message : messages) {
            saveMessage(conversationId, message);
        }
    }

    @Override
    public void deleteByConversationId(String conversationId) {
        jdbcTemplate.update(
                "DELETE FROM spring_ai_chat_memory_enhanced WHERE conversation_id = ?",
                conversationId
        );
    }

    public RatableMessage getMessageById(String messageId) {
        List<Message> messages = jdbcTemplate.query("""
                        SELECT * FROM spring_ai_chat_memory_enhanced 
                        WHERE id = ?
                        """,
                new RatableMessageRowMapper(),
                messageId
        );

        return messages.isEmpty() ? null : (RatableMessage) messages.get(0);
    }

    public List<Message> getRecentMessages(String conversationId, int limit) {
        return jdbcTemplate.query("""
                        SELECT * FROM spring_ai_chat_memory_enhanced 
                        WHERE conversation_id = ? 
                        ORDER BY created_at DESC 
                        LIMIT ?
                        """,
                new RatableMessageRowMapper(),
                conversationId,
                limit
        );
    }

    public void saveMessage(String conversationId, Message message) {
        String messageId;

        if (message instanceof RatableMessage ratableMessage) {
            messageId = ratableMessage.getMessageId();
            message = ratableMessage.getOriginalMessage();
        } else {
            messageId = UUID.randomUUID().toString();
        }

        Integer count = jdbcTemplate.queryForObject("""
                SELECT COUNT(*) FROM spring_ai_chat_memory_enhanced WHERE id = ?
                """, Integer.class, messageId);

        if (count != null && count > 0) {
            jdbcTemplate.update("""
                            UPDATE spring_ai_chat_memory_enhanced 
                            SET content = ?, metadata = ? 
                            WHERE id = ?
                            """,
                    message.getText(),
                    serializeMetadata(message.getMetadata()),
                    messageId
            );
        } else {
            jdbcTemplate.update("""
                            INSERT INTO spring_ai_chat_memory_enhanced 
                            (id, conversation_id, message_type, content, metadata) 
                            VALUES (?, ?, ?, ?, ?)
                            """,
                    messageId,
                    conversationId,
                    message.getMessageType().name(),
                    message.getText(),
                    serializeMetadata(message.getMetadata())
            );
        }
    }

    private String serializeMetadata(Map<String, Object> metadata) {
        try {
            return objectMapper.writeValueAsString(metadata);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> deserializeMetadata(String metadata) {
        try {
            return objectMapper.readValue(metadata, Map.class);
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private class RatableMessageRowMapper implements RowMapper<Message> {
        @Override
        public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
            MessageType type = MessageType.valueOf(rs.getString("message_type"));
            String content = rs.getString("content");
            String messageId = rs.getString("id");
            String conversationId = rs.getString("conversation_id");
            String metadataJson = rs.getString("metadata");

            Message originalMessage = switch (type) {
//                case USER -> new UserMessage(content, null, deserializeMetadata(metadataJson));
                case USER -> UserMessage.builder()
                        .text(content)
                        .metadata(deserializeMetadata(metadataJson))
                        .build();
                case ASSISTANT -> new AssistantMessage(content, deserializeMetadata(metadataJson));
//                case SYSTEM -> new SystemMessage(content, deserializeMetadata(metadataJson));
                case SYSTEM -> SystemMessage.builder()
                        .text(content)
                        .metadata(deserializeMetadata(metadataJson))
                        .build();
                case TOOL -> new ToolResponseMessage(new ArrayList<>(), deserializeMetadata(metadataJson));
            };

            return new RatableMessage(originalMessage, messageId, conversationId);
        }
    }
}
