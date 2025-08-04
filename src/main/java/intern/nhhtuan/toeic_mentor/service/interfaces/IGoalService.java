package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.request.GoalCreateRequest;
import intern.nhhtuan.toeic_mentor.dto.request.GoalUpdateRequest;
import intern.nhhtuan.toeic_mentor.dto.response.GoalResponse;
import intern.nhhtuan.toeic_mentor.dto.response.TestResultResponse;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface IGoalService {
    List<GoalResponse> getTodayGoals(String email);

    List<GoalResponse> getAllGoals(String email);

    boolean createGoal(String email, GoalCreateRequest goalCreateRequest);

    boolean updateGoal(Long id, GoalUpdateRequest goalUpdateRequest);

    boolean updateGoalStatus(Long id);

    boolean deleteGoal(Long id);

    @Async
    void updateGoalProgressAfterTest(String email, TestResultResponse testResultResponse);
}
