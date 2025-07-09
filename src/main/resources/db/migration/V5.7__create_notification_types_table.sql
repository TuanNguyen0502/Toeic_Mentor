USE
`toeic-mentor`;

-- 1. Tạo bảng "notification_types"
CREATE TABLE notification_types
(
    action      VARCHAR(100) PRIMARY KEY,
    description VARCHAR(255)
);

-- 2. Thêm dữ liệu mẫu vào bảng "notification_types"
INSERT INTO notification_types (action, description) VALUES
('NEW_REPORT', 'Thông báo có báo cáo lỗi mới'),
('COMPLETE_REPORT', 'Thông báo có phản hồi về báo cáo lỗi');