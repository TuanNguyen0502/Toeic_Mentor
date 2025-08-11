USE
`toeic-mentor`;

-- 1. Thêm cột mới tags vào bảng questions
ALTER TABLE questions
    ADD COLUMN tags JSON;
