package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.request.AnswerRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ITestService {
    int countCorrectByCombinePartsAndPercent(List<Long> partIds, int percent);

    int getTotalTests();

    @Transactional
    void saveTests(String email, List<AnswerRequest> answerRequests);
}
