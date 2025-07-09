USE
`toeic-mentor`;

-- 2. Cập nhật bảng "answers"
-- 2.1 Thêm cột is_correct
ALTER TABLE answers
    ADD COLUMN is_correct BOOLEAN NOT NULL DEFAULT FALSE;
-- 2.2 Thêm cột explanation
ALTER TABLE answers
    ADD COLUMN explanation TEXT;