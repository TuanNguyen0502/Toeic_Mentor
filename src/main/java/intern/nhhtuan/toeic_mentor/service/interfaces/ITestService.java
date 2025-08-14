package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.request.TestCountRequest;
import intern.nhhtuan.toeic_mentor.dto.request.UncompletedAnswerRequest;
import intern.nhhtuan.toeic_mentor.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface ITestService {
    Page<TestHistoryResponse> getTestHistoryResponses(String email,
                                                      LocalDateTime createdAtStart,
                                                      LocalDateTime createdAtEnd,
                                                      Boolean completed,
                                                      int page,
                                                      int size,
                                                      String sortBy,
                                                      String direction);

    TestHistoryDetailResponse getTestHistoryDetailResponseById(Long id);

    List<TestCountResponse> countByPartsAndPercent(TestCountRequest testCountRequest);

    int getTotalTests();

    @Transactional
    void saveTest(String email, TestResultResponse testResultResponse);

    void saveUncompletedTest(String email, List<UncompletedAnswerRequest> uncompletedAnswerRequests);

    TestResultResponse getTestResult(Long testId, String email);

    List<RecentTestResponse> getRecentTests(String email, int number);

    TestStatisticResponse calculateTestStatistic(String email);


}
