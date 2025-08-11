USE
`toeic-mentor`;

-- 3. Update "questions" table to reference "parts"
-- 3.1 Thêm cột part_id mới
ALTER TABLE questions
    ADD COLUMN part_id BIGINT;

-- 3.2 Thêm ràng buộc khóa ngoại tới bảng parts
ALTER TABLE questions
    ADD CONSTRAINT fk_questions_part
        FOREIGN KEY (part_id) REFERENCES parts (id);