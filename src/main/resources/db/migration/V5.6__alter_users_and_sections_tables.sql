USE
`toeic-mentor`;


-- 2. Cập nhật bảng "users" và "sections"
-- 2.1 Thêm sửa cột created_at và updated_at
ALTER TABLE users
    MODIFY COLUMN created_at DATETIME,
    MODIFY COLUMN updated_at DATETIME;

ALTER TABLE sections
    MODIFY COLUMN created_at DATETIME,
    MODIFY COLUMN updated_at DATETIME;

