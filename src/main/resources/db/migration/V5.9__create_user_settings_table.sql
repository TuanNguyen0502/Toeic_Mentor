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

-- 2. Thêm dữ liệu mẫu vào bảng "user_settings"
INSERT INTO user_settings (user_id, language, theme) VALUES
(1, 'en', 'light'),
(2, 'vi', 'dark'),
(3, 'fr', 'light');