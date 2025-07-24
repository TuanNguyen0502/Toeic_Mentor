USE
`toeic-mentor`;

-- 1. Tạo bảng "chatbot_feedbacks"
CREATE TABLE chatbot_feedbacks
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id             BIGINT NOT NULL,
    chatbot_response_id BIGINT NOT NULL,
    feedback            ENUM('LIKE', 'DISLIKE') NOT NULL,
    created_at          DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (chatbot_response_id) REFERENCES SPRING_AI_CHAT_MEMORY (id)
);