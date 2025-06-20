USE
`toeic-mentor`;

-- 4. Create "tests" table
CREATE TABLE tests
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id    BIGINT   NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);