package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.request.AnswerRequest;
import intern.nhhtuan.toeic_mentor.dto.request.TestCountRequest;
import intern.nhhtuan.toeic_mentor.dto.response.TestCountResponse;
import intern.nhhtuan.toeic_mentor.dto.response.TestResultResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ITestService {
    List<TestCountResponse> countByPartsAndPercent(TestCountRequest testCountRequest);

    int getTotalTests();

    @Transactional
    void saveTest(String email, TestResultResponse testResultResponse);

    TestResultResponse getTestResult(Long testId, String email);
}
