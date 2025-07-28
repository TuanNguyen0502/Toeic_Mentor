USE
`toeic-mentor`;

CREATE TABLE user_statistics
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT NOT NULL,
    estimated_score INT      DEFAULT 0,
    score_interval  INT      DEFAULT 0,
    accuracy        INT      DEFAULT 0,
    correct_answers INT      DEFAULT 0,
    total_answers   INT      DEFAULT 0,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);