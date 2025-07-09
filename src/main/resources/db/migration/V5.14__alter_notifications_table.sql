USE
`toeic-mentor`;

-- ==========================================
-- 1️⃣ Thêm cột tạm notification_type_id
-- ==========================================
ALTER TABLE notifications
    ADD COLUMN notification_type_id BIGINT;

-- ==========================================
-- 2️⃣ Copy dữ liệu từ action sang id
-- ==========================================
UPDATE notifications n
    JOIN notification_types t
ON n.action = t.action
    SET n.notification_type_id = t.id;

-- ==========================================
-- 4️⃣ Drop cột action cũ
ALTER TABLE notifications
DROP
COLUMN action;

-- ==========================================
-- 5️⃣ Đổi tên cột notification_type_id thành action (nếu bạn muốn giữ tên cũ)
-- Hoặc để tên là notification_type_id cho rõ ràng
ALTER TABLE notifications
    CHANGE COLUMN notification_type_id notification_type_id BIGINT;

-- ==========================================
-- 6️⃣ Tạo FOREIGN KEY mới FK tới notification_types(id)
ALTER TABLE notifications
    ADD CONSTRAINT fk_notifications_notification_type
        FOREIGN KEY (notification_type_id)
            REFERENCES notification_types (id);
