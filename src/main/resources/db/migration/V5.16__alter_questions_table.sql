USE
`toeic-mentor`;

-- 1. Thêm cột mới tags vào bảng questions
ALTER TABLE questions
    ADD COLUMN tags JSON;

-- 2. Chuyển dữ liệu từ question_tags sang questions.tags
UPDATE questions q
SET q.tags = (SELECT JSON_ARRAYAGG(qt.tag)
              FROM question_tags qt
              WHERE qt.question_id = q.id);
