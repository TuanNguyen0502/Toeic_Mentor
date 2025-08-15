ALTER TABLE answers
DROP
FOREIGN KEY answers_ibfk_2;

ALTER TABLE answers
    ADD CONSTRAINT answers_ibfk_2
        FOREIGN KEY (test_id) REFERENCES tests (id)
            ON DELETE CASCADE;

ALTER TABLE tests_parts
DROP
FOREIGN KEY tests_parts_ibfk_1;

ALTER TABLE tests_parts
    ADD CONSTRAINT tests_parts_ibfk_1
        FOREIGN KEY (test_id) REFERENCES tests (id)
            ON DELETE CASCADE;