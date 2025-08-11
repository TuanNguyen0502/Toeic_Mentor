USE
`toeic-mentor`;

-- 1. Tạo bảng "user_settings"
CREATE TABLE user_settings
(
    user_id BIGINT PRIMARY KEY,
    language VARCHAR(50) DEFAULT 'en',
    theme VARCHAR(50) DEFAULT 'light',
    FOREIGN KEY (user_id) REFERENCES users (id)
);