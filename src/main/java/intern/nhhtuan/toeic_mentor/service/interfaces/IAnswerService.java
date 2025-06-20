package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.entity.Answer;

public interface IAnswerService {
    boolean isCorrect(Long answerId);

    void save(Answer answer);
}
