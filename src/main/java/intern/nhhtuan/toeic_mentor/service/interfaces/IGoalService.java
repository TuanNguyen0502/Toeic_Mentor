package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.request.GoalCreateRequest;
import intern.nhhtuan.toeic_mentor.dto.response.GoalResponse;

import java.util.List;

public interface IGoalService {
    List<GoalResponse> getTodayGoals(String email);

    boolean createGoal(String email, GoalCreateRequest goalCreateRequest);
}
