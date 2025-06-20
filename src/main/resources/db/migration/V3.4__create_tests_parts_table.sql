USE
`toeic-mentor`;

-- 6. Create "tests_parts" table (nhiều-nhiều giữa tests và parts)
CREATE TABLE tests_parts
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    test_id BIGINT NOT NULL,
    part_id BIGINT NOT NULL,
    FOREIGN KEY (test_id) REFERENCES tests (id),
    FOREIGN KEY (part_id) REFERENCES parts (id)
);