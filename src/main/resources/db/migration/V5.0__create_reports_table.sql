USE
`toeic-mentor`;

-- 1. Tạo bảng "reports"
CREATE TABLE reports
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT   NOT NULL,
    question_id BIGINT   NOT NULL,
    category    ENUM('WRONG_QUESTION', 'TYPING_ERROR', 'UNCLEAR_ANSWER', 'OTHER') NOT NULL,
    description text     NOT NULL,
    status      ENUM('OPEN', 'RESOLVED', 'REJECTED') NOT NULL,
    created_at  DATETIME NOT NULL,
    updated_at  DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
);