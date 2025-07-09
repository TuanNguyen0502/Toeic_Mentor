USE
`toeic-mentor`;

-- ===========================================
-- 1️⃣ Drop các bảng con trước
-- ===========================================
DROP TABLE IF EXISTS role_notifications;
DROP TABLE IF EXISTS notification_settings;

-- ===========================================
-- 2️⃣ Drop bảng notification_types
-- ===========================================
DROP TABLE IF EXISTS notification_types;

-- ===========================================
-- 3️⃣ Tạo bảng notification_types chuẩn hoá
-- ===========================================
CREATE TABLE notification_types
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    action      ENUM('NEW_REPORT', 'COMPLETE_REPORT') UNIQUE NOT NULL,
    description VARCHAR(255)
);

-- ===========================================
-- 4️⃣ Insert dữ liệu mẫu notification_types
-- ===========================================
INSERT INTO notification_types (action, description)
VALUES ('NEW_REPORT', 'Thông báo có báo cáo lỗi mới'),
       ('COMPLETE_REPORT', 'Thông báo có phản hồi về báo cáo lỗi');

-- ===========================================
-- 5️⃣ Tạo bảng notification_settings (FK -> id)
-- ===========================================
CREATE TABLE notification_settings
(
    user_id              BIGINT NOT NULL,
    notification_type_id BIGINT NOT NULL,
    enabled              BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (user_id, notification_type_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (notification_type_id) REFERENCES notification_types (id)
);

-- ===========================================
-- 6️⃣ Insert dữ liệu mẫu notification_settings
-- ===========================================
-- Chú ý: cần mapping id dựa trên enum action
INSERT INTO notification_settings (user_id, notification_type_id, enabled)
SELECT 1, id, TRUE
FROM notification_types
WHERE action ='NEW_REPORT'
UNION ALL
SELECT 1, id, TRUE
FROM notification_types
WHERE action ='COMPLETE_REPORT'
UNION ALL
SELECT 2, id, TRUE
FROM notification_types
WHERE action ='NEW_REPORT'
UNION ALL
SELECT 2, id, TRUE
FROM notification_types
WHERE action ='COMPLETE_REPORT'
UNION ALL
SELECT 3, id, TRUE
FROM notification_types
WHERE action ='NEW_REPORT'
UNION ALL
SELECT 3, id, TRUE
FROM notification_types
WHERE action ='COMPLETE_REPORT';

-- ===========================================
-- 7️⃣ Tạo bảng role_notifications (FK -> id)
-- ===========================================
CREATE TABLE role_notifications
(
    role_id              BIGINT NOT NULL,
    notification_type_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, notification_type_id),
    FOREIGN KEY (role_id) REFERENCES roles (id),
    FOREIGN KEY (notification_type_id) REFERENCES notification_types (id)
);

-- ===========================================
-- 8️⃣ Insert dữ liệu mẫu role_notifications
-- ===========================================
INSERT INTO role_notifications (role_id, notification_type_id)
SELECT 1, id
FROM notification_types
WHERE action ='NEW_REPORT'
UNION ALL
SELECT 1, id
FROM notification_types
WHERE action ='COMPLETE_REPORT'
UNION ALL
SELECT 2, id
FROM notification_types
WHERE action ='COMPLETE_REPORT';