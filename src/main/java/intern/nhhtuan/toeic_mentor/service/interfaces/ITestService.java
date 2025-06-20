package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.request.AnswerRequest;
import intern.nhhtuan.toeic_mentor.dto.request.TestCountRequest;
import intern.nhhtuan.toeic_mentor.dto.response.TestCountResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ITestService {
    List<TestCountResponse> countByCombinePartsAndPercent(TestCountRequest testCountRequest);

    int getTotalTests();

    @Transactional
    void saveTests(String email, List<AnswerRequest> answerRequests);
}
