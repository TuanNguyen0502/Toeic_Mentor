package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.request.TestCountRequest;
import intern.nhhtuan.toeic_mentor.entity.Answer;

import java.util.List;

public interface IAnswerService {
    boolean checkByStatus(Long answerId, TestCountRequest.EStatus status);

    void save(Answer answer);

    void saveAll(List<Answer> answers);

    void deleteAllByTestId(Long testId);
}
