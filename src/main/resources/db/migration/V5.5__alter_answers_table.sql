USE
`toeic-mentor`;

-- 2. Cập nhật bảng "answers"
-- 2.1 Xoa cột explanation
ALTER TABLE answers
DROP
COLUMN explanation;