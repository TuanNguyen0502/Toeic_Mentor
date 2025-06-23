USE
`toeic-mentor`;

-- 2. Cập nhật bảng "questions"
-- 2.1 Thêm cột section_id (nullable để giữ backward compatibility nếu cần)
ALTER TABLE questions
    ADD COLUMN section_id BIGINT;

-- 2.2 Thêm ràng buộc khóa ngoại tới bảng sections
ALTER TABLE questions
    ADD CONSTRAINT fk_questions_section
        FOREIGN KEY (section_id) REFERENCES sections (id);

-- 2.3 Thêm cột status cho câu hỏi
ALTER TABLE questions
    ADD COLUMN status ENUM('IN_SECTION', 'APPROVED', 'DELETED');

-- 2.4 Nếu có dữ liệu cũ, cập nhật dữ liệu như sau
UPDATE questions
SET section_id = (SELECT id FROM sections WHERE sections.title = 'Section Sample');
UPDATE questions
SET status = 'APPROVED';
