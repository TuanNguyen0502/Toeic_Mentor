USE
`toeic-mentor`;

-- Thay đổi kiểu dữ liệu của created_at và updated_at thành DATE
ALTER TABLE sections
    MODIFY COLUMN created_at DATE NOT NULL,
    MODIFY COLUMN updated_at DATE NOT NULL;
