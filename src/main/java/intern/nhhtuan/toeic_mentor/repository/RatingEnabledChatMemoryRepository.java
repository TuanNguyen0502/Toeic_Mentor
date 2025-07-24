package intern.nhhtuan.toeic_mentor.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import intern.nhhtuan.toeic_mentor.entity.RatableMessage;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class RatingEnabledChatMemoryRepository implements ChatMemoryRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public RatingEnabledChatMemoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public boolean renameConversationId(String oldConversationId, String newConversationId) {
        int updatedRows = jdbcTemplate.update("""
                UPDATE spring_ai_chat_memory_enhanced 
                SET conversation_id = ? 
                WHERE conversation_id = ?
                """, newConversationId, oldConversationId);
        return updatedRows > 0;
    }

    public boolean existsByConversationId(String conversationId) {
        Integer count = jdbcTemplate.queryForObject("""
                SELECT COUNT(*) FROM spring_ai_chat_memory_enhanced 
                WHERE conversation_id = ?
                """, Integer.class, conversationId);
        return count != null && count > 0;
    }

    public List<String> getChatHistory(String conversationId) {
        return jdbcTemplate.queryForList("""
                SELECT content 
                FROM spring_ai_chat_memory_enhanced 
                WHERE conversation_id = ? 
                ORDER BY created_at ASC
                """, String.class, conversationId);
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

    public void rateMessage(String messageId, String rating, String feedback) {
        if (!Arrays.asList("like", "dislike").contains(rating)) {
            throw new IllegalArgumentException("Rating must be 'like' or 'dislike'");
        }

        int updatedRows = jdbcTemplate.update("""
                        UPDATE spring_ai_chat_memory_enhanced 
                        SET rating = ?, feedback = ?, rated_at = CURRENT_TIMESTAMP 
                        WHERE id = ?
                        """,
                rating, feedback, messageId
        );

        if (updatedRows == 0) {
            throw new IllegalArgumentException("Message with ID " + messageId + " not found");
        }
    }

    public List<Message> getMessagesByRating(String conversationId, String rating) {
        return jdbcTemplate.query("""
                        SELECT * FROM spring_ai_chat_memory_enhanced 
                        WHERE conversation_id = ? AND rating = ? 
                        ORDER BY created_at DESC
                        """,
                new RatableMessageRowMapper(),
                conversationId,
                rating
        );
    }

    public Map<String, Integer> getRatingStats(String conversationId) {
        List<Map<String, Object>> results = jdbcTemplate.queryForList("""
                        SELECT rating, COUNT(*) as count 
                        FROM spring_ai_chat_memory_enhanced 
                        WHERE conversation_id = ? AND rating IS NOT NULL 
                        GROUP BY rating
                        """,
                conversationId
        );

        return results.stream()
                .collect(Collectors.toMap(
                        row -> (String) row.get("rating"),
                        row -> ((Number) row.get("count")).intValue()
                ));
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

    private void saveMessage(String conversationId, Message message) {
        String messageId;
        String rating = null;
        String feedback = null;
        Timestamp ratedAt = null;

        if (message instanceof RatableMessage ratableMessage) {
            messageId = ratableMessage.getMessageId();
            rating = ratableMessage.getRating();
            feedback = ratableMessage.getFeedback();
            ratedAt = ratableMessage.getRatedAt() != null ?
                    Timestamp.valueOf(ratableMessage.getRatedAt()) : null;
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
                            SET content = ?, metadata = ?, rating = ?, feedback = ?, rated_at = ?
                            WHERE id = ?
                            """,
                    message.getText(),
                    serializeMetadata(message.getMetadata()),
                    rating,
                    feedback,
                    ratedAt,
                    messageId
            );
        } else {
            jdbcTemplate.update("""
                            INSERT INTO spring_ai_chat_memory_enhanced 
                            (id, conversation_id, message_type, content, metadata, rating, feedback, rated_at) 
                            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                            """,
                    messageId,
                    conversationId,
                    message.getMessageType().name(),
                    message.getText(),
                    serializeMetadata(message.getMetadata()),
                    rating,
                    feedback,
                    ratedAt
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

            RatableMessage ratableMessage = new RatableMessage(originalMessage, messageId, conversationId);

            String rating = rs.getString("rating");
            String feedback = rs.getString("feedback");
            Timestamp ratedAt = rs.getTimestamp("rated_at");

            if (rating != null) {
                ratableMessage.setRating(rating, feedback);
                if (ratedAt != null) {
                    ratableMessage.setRatedAt(ratedAt.toLocalDateTime());
                }
            }

            return ratableMessage;
        }
    }
}
