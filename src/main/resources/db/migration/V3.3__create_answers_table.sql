USE
`toeic-mentor`;

-- 5. Create "answers" table
CREATE TABLE answers
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    answer      VARCHAR(1) NOT NULL,
    question_id BIGINT     NOT NULL,
    test_id     BIGINT     NOT NULL,
    FOREIGN KEY (question_id) REFERENCES questions (id),
    FOREIGN KEY (test_id) REFERENCES tests (id)
);