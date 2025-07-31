package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.request.GoalCreateRequest;
import intern.nhhtuan.toeic_mentor.dto.response.GoalResponse;
import intern.nhhtuan.toeic_mentor.entity.Goal;
import intern.nhhtuan.toeic_mentor.entity.enums.EGoalStatus;
import intern.nhhtuan.toeic_mentor.entity.enums.EGoalUnit;
import intern.nhhtuan.toeic_mentor.exception.UnauthorizedException;
import intern.nhhtuan.toeic_mentor.repository.GoalRepository;
import intern.nhhtuan.toeic_mentor.repository.UserRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.IGoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements IGoalService {
    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    @Override
    public List<GoalResponse> getTodayGoals(String email) {
        // Fetch today's goals from the repository
        LocalDate now = LocalDate.now();
        return goalRepository.findByUser_EmailAndGoalDate(email, now)
                .stream()
                .map(goal -> GoalResponse.builder()
                        .id(goal.getId())
                        .title(goal.getTitle())
                        .type(goal.getType().name())
                        .goalDate(goal.getGoalDate())
                        .targetValue(goal.getTargetValue())
                        .actualValue(goal.getActualValue())
                        .unit(goal.getUnit().name())
                        .part(goal.getPart())
                        .status(goal.getStatus().name())
                        .build())
                .toList();
    }

    @Override
    public List<GoalResponse> getAllGoals(String email) {
        // Fetch all goals from the repository
        return goalRepository.findByUser_Email(email)
                .stream()
                .map(goal -> GoalResponse.builder()
                        .id(goal.getId())
                        .title(goal.getTitle())
                        .type(goal.getType().name())
                        .goalDate(goal.getGoalDate())
                        .targetValue(goal.getTargetValue())
                        .actualValue(goal.getActualValue())
                        .unit(goal.getUnit().name())
                        .part(goal.getPart())
                        .status(goal.getStatus().name())
                        .build())
                .toList();
    }

    @Override
    public boolean createGoal(String email, GoalCreateRequest goalCreateRequest) {
        Goal goal = new Goal();
        goal.setTitle(goalCreateRequest.getTitle());
        goal.setType(goalCreateRequest.getType());
        goal.setGoalDate(LocalDate.now());
        goal.setTargetValue(goalCreateRequest.getTargetValue());
        goal.setActualValue(0);
        goal.setUnit(goalCreateRequest.getUnit());
        if (EGoalUnit.QUESTIONS.equals(goalCreateRequest.getUnit()) && goalCreateRequest.getPart() != null) {
            goal.setPart(goalCreateRequest.getPart());
        } else {
            goal.setPart(null);
        }
        goal.setStatus(EGoalStatus.IN_PROGRESS);
        goal.setUser(userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found with email: " + email)));
        goalRepository.save(goal);
        return true;
    }
}
