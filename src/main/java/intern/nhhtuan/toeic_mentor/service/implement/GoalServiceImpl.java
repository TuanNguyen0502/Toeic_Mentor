package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.request.GoalCreateRequest;
import intern.nhhtuan.toeic_mentor.dto.request.GoalUpdateRequest;
import intern.nhhtuan.toeic_mentor.dto.response.GoalResponse;
import intern.nhhtuan.toeic_mentor.entity.Goal;
import intern.nhhtuan.toeic_mentor.entity.enums.EGoalStatus;
import intern.nhhtuan.toeic_mentor.entity.enums.EGoalType;
import intern.nhhtuan.toeic_mentor.entity.enums.EGoalUnit;
import intern.nhhtuan.toeic_mentor.exception.ResourceNotFoundException;
import intern.nhhtuan.toeic_mentor.exception.UnauthorizedException;
import intern.nhhtuan.toeic_mentor.repository.GoalRepository;
import intern.nhhtuan.toeic_mentor.repository.UserRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.IGoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                        .part(goal.getPart() != null ? goal.getPart() : 0)
                        .status(goal.getStatus().name())
                        .build())
                .toList();
    }

    @Override
    public List<GoalResponse> getAllGoals(String email) {
        // Fetch all goals from the repository
        return goalRepository.findByUser_EmailOrderByGoalDateDesc(email)
                .stream()
                .map(goal -> GoalResponse.builder()
                        .id(goal.getId())
                        .title(goal.getTitle())
                        .type(goal.getType().name())
                        .goalDate(goal.getGoalDate())
                        .targetValue(goal.getTargetValue())
                        .actualValue(goal.getActualValue())
                        .unit(goal.getUnit().name())
                        .part(goal.getPart() != null ? goal.getPart() : 0)
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

    @Override
    public boolean updateGoal(Long id, GoalUpdateRequest goalUpdateRequest) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "id", id));
        if (goalUpdateRequest.getActualValue() > goalUpdateRequest.getTargetValue()) {
            throw new ResourceNotFoundException("Goal", "actual value", goalUpdateRequest.getActualValue());
        }

        goal.setTitle(goalUpdateRequest.getTitle());
        goal.setType(goalUpdateRequest.getType());
        goal.setTargetValue(goalUpdateRequest.getTargetValue());
        goal.setActualValue(goalUpdateRequest.getActualValue());

        if (goalUpdateRequest.getActualValue() >= goalUpdateRequest.getTargetValue()) {
            goal.setStatus(EGoalStatus.COMPLETED);
        } else {
            goal.setStatus(EGoalStatus.IN_PROGRESS);
        }

        goal.setUnit(goalUpdateRequest.getUnit());

        if (EGoalUnit.QUESTIONS.equals(goalUpdateRequest.getUnit()) && goalUpdateRequest.getPart() != null) {
            goal.setPart(goalUpdateRequest.getPart());
        } else {
            goal.setPart(null);
        }

        goal.setStatus(goalUpdateRequest.getStatus());

        goalRepository.save(goal);
        return true;
    }

    @Override
    public boolean updateGoalStatus(Long id) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "id", id));

        if (goal.getStatus().equals(EGoalStatus.COMPLETED)) {
            goal.setStatus(EGoalStatus.IN_PROGRESS);
        } else if (goal.getStatus().equals(EGoalStatus.IN_PROGRESS)) {
            goal.setStatus(EGoalStatus.COMPLETED);
        }

        goalRepository.save(goal);
        return true;
    }

    @Override
    public boolean deleteGoal(Long id) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "id", id));
        goalRepository.delete(goal);
        return true;
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Ho_Chi_Minh")
    public void repeatDailyGoal() {
        // This method is scheduled to run at midnight every day
        // It will find all daily goals from yesterday, mark them as completed,
        // and create new goals for today with the same properties but with status IN_PROGRESS.

        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);

        // Fetch all daily goals from yesterday
        List<Goal> goals = goalRepository.findAllByTypeAndGoalDate(EGoalType.DAILY, yesterday);
        for (Goal goal : goals) {
            // Update the existing goal's status to COMPLETED if it was not already completed
            if (EGoalStatus.IN_PROGRESS.equals(goal.getStatus())) {
                if (goal.getActualValue() < goal.getTargetValue()) {
                    goal.setStatus(EGoalStatus.FAILED);
                } else {
                    goal.setStatus(EGoalStatus.COMPLETED);
                }
            }

            // Create a new goal for today with the same properties as the existing goal
            // but with the status set to IN_PROGRESS
            Goal newGoal = new Goal();
            newGoal.setTitle(goal.getTitle());
            newGoal.setType(goal.getType());
            newGoal.setGoalDate(today);
            newGoal.setTargetValue(goal.getTargetValue());
            newGoal.setActualValue(0);
            newGoal.setUnit(goal.getUnit());
            if (EGoalUnit.QUESTIONS.equals(goal.getUnit()) && goal.getPart() != null) {
                newGoal.setPart(goal.getPart());
            } else {
                newGoal.setPart(null);
            }
            newGoal.setStatus(EGoalStatus.IN_PROGRESS);
            newGoal.setUser(goal.getUser());
            goalRepository.save(newGoal);
        }
    }
}
