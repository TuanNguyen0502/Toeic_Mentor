USE
`toeic-mentor`;

-- 3. Update "questions" table to reference "parts"
-- 3.1 Thêm cột part_id mới
ALTER TABLE questions
    ADD COLUMN part_id BIGINT;

-- 3.2 Nếu có dữ liệu cũ, cập nhật dữ liệu như sau
UPDATE questions
SET part_id = (SELECT id FROM parts WHERE parts.name = CONCAT('PART_', questions.part));

-- 3.3 Xóa cột "part" cũ (int)
ALTER TABLE questions
DROP
COLUMN part;

-- 3.4 Thêm ràng buộc khóa ngoại tới bảng parts
ALTER TABLE questions
    ADD CONSTRAINT fk_questions_part
        FOREIGN KEY (part_id) REFERENCES parts (id);