USE
`toeic-mentor`;

CREATE TABLE chatbot_rating
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT       NOT NULL,
    message_id VARCHAR(255) NOT NULL,
    rating     ENUM('LIKE', 'DISLIKE') NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (message_id) REFERENCES spring_ai_chat_memory_enhanced (id)
);