USE
`toeic-mentor`;

-- 2. Cập nhật bảng "questions"
-- 2.1 Thêm cột answer_explanation
ALTER TABLE questions
    ADD COLUMN answer_explanation TEXT;