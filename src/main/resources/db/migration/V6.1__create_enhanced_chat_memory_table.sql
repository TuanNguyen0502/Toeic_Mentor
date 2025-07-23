CREATE TABLE spring_ai_chat_memory_enhanced
(
    id              VARCHAR(255) PRIMARY KEY,
    conversation_id VARCHAR(255) NOT NULL,
    message_type    VARCHAR(50)  NOT NULL,
    content         TEXT         NOT NULL,
    metadata        TEXT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    rating          VARCHAR(20),
    feedback        TEXT,
    rated_at        TIMESTAMP
);

CREATE INDEX idx_conversation_id ON spring_ai_chat_memory_enhanced (conversation_id);
CREATE INDEX idx_rating ON spring_ai_chat_memory_enhanced (rating);
CREATE INDEX idx_created_at ON spring_ai_chat_memory_enhanced (created_at);
CREATE INDEX idx_message_type ON spring_ai_chat_memory_enhanced (message_type);