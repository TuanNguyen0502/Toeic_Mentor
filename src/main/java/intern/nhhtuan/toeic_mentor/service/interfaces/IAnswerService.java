package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.request.TestCountRequest;
import intern.nhhtuan.toeic_mentor.entity.Answer;

public interface IAnswerService {
    boolean checkByStatus(Long answerId, TestCountRequest.EStatus status);

    void save(Answer answer);
}
