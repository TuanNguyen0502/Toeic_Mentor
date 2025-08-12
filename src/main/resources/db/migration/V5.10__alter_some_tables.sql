USE
`toeic-mentor`;

ALTER TABLE tests
    ADD COLUMN performance VARCHAR(255);
ALTER TABLE tests
    ADD COLUMN referenceUrls JSON;

ALTER TABLE answers
    ADD COLUMN answerExplanation JSON;
ALTER TABLE answers
    ADD COLUMN time_spent INT;

ALTER TABLE questions
    ADD COLUMN difficulty INT;