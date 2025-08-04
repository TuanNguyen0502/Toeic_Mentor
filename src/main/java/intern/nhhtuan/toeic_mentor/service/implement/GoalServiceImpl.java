package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.request.GoalCreateRequest;
import intern.nhhtuan.toeic_mentor.dto.request.GoalUpdateRequest;
import intern.nhhtuan.toeic_mentor.dto.response.GoalResponse;
import intern.nhhtuan.toeic_mentor.dto.response.TestResultResponse;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
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
        LocalDate thisWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        List<Goal> dailyGoals = goalRepository.findByUser_EmailAndTypeAndGoalDate(email, EGoalType.DAILY, now);
        List<Goal> weeklyGoals = goalRepository.findByUser_EmailAndTypeAndGoalDate(email, EGoalType.WEEKLY, thisWeek);

        // Combine today's goals and weekly goals
        dailyGoals.addAll(weeklyGoals);

        return dailyGoals.stream()
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

        // Set the goal date based on the type of goal
        goal.setGoalDate(getGoalDateByType(goalCreateRequest.getType()));

        goal.setTargetValue(goalCreateRequest.getTargetValue());
        goal.setActualValue(0);
        goal.setUnit(goalCreateRequest.getUnit());

        // Set the part if the unit is QUESTIONS or PARTS and part is provided
        // Otherwise, set it to null
        if ((EGoalUnit.QUESTIONS.equals(goalCreateRequest.getUnit()) || EGoalUnit.PARTS.equals(goalCreateRequest.getUnit()))
                && goalCreateRequest.getPart() != null) {
            goal.setPart(goalCreateRequest.getPart());
        } else {
            goal.setPart(null);
        }

        // Set the initial status to IN_PROGRESS
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

        goal.setTitle(goalUpdateRequest.getTitle());
        goal.setType(goalUpdateRequest.getType());

        // Set the goal date based on the type of goal
        goal.setGoalDate(getGoalDateByType(goalUpdateRequest.getType()));

        goal.setTargetValue(goalUpdateRequest.getTargetValue());

        // Update the actual value based on the unit type
        if (EGoalUnit.WORDS.equals(goal.getUnit()) || EGoalUnit.OTHER.equals(goal.getUnit())) {
            goal.setActualValue(goalUpdateRequest.getActualValue());
        }

        // Update status based on the actual value and target value
        if (goal.getActualValue() >= goal.getTargetValue()) {
            goal.setStatus(EGoalStatus.COMPLETED);
        } else {
            goal.setStatus(EGoalStatus.IN_PROGRESS);
        }

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
                updateGoalStatusByActualValue(goal);
            }

            // Create a new goal for today with the same properties as the existing goal
            duplicateGoal(goal, today);
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * MON", zone = "Asia/Ho_Chi_Minh")
    public void repeatWeeklyGoal() {
        // This method is scheduled to run at midnight every Monday
        // It will find all daily goals from yesterday, mark them as completed,
        // and create new goals for today with the same properties but with status IN_PROGRESS.

        LocalDate thisWeek = LocalDate.now();
        LocalDate lastWeek = LocalDate.now().minusDays(7);

        // Fetch all daily goals from last week
        List<Goal> goals = goalRepository.findAllByTypeAndGoalDate(EGoalType.WEEKLY, lastWeek);
        for (Goal goal : goals) {
            // Update the existing goal's status to COMPLETED if it was not already completed
            if (EGoalStatus.IN_PROGRESS.equals(goal.getStatus())) {
                updateGoalStatusByActualValue(goal);
            }

            // Create a new goal for this week with the same properties as the existing goal
            duplicateGoal(goal, thisWeek);
        }
    }

    @Async
    @Override
    public void updateGoalProgressAfterTest(String email, TestResultResponse testResultResponse) {
        // This method updates the goal progress after a test result is submitted
        // It will find the goal for the specific part and update its actual value accordingly

        LocalDate today = LocalDate.now();
        LocalDate thisWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        // Fetch all daily goals for today
        List<Goal> todayGoals = goalRepository
                .findAllByUser_EmailAndTypeAndGoalDateAndStatus(email, EGoalType.DAILY, today, EGoalStatus.IN_PROGRESS);
        // Fetch all weekly goals for this week
        List<Goal> weeklyGoals = goalRepository
                .findAllByUser_EmailAndTypeAndGoalDateAndStatus(email, EGoalType.WEEKLY, thisWeek, EGoalStatus.IN_PROGRESS);
        // Combine today's goals and weekly goals
        todayGoals.addAll(weeklyGoals);
        if (todayGoals.isEmpty()) {
            // If there are no goals for today, return early
            return;
        }

        // Filter goals by unit type
        List<Goal> minuteGoals = todayGoals.stream()
                .filter(goal -> EGoalUnit.MINUTES.equals(goal.getUnit()))
                .toList();
        List<Goal> questionGoals = todayGoals.stream()
                .filter(goal -> EGoalUnit.QUESTIONS.equals(goal.getUnit()))
                .toList();
        List<Goal> partGoals = todayGoals.stream()
                .filter(goal -> EGoalUnit.PARTS.equals(goal.getUnit()))
                .toList();
        List<Goal> testGoals = todayGoals.stream()
                .filter(goal -> EGoalUnit.TESTS.equals(goal.getUnit()))
                .toList();

        // Get distinct parts from the test result
        List<Integer> parts = testResultResponse.getAnswerResponses()
                .stream()
                .map(TestResultResponse.AnswerResponse::getPart)
                .distinct()
                .toList();

        // Update minute goals
        for (Goal goal : minuteGoals) {
            int minutesSpent = 0;
            for (TestResultResponse.AnswerResponse answer : testResultResponse.getAnswerResponses()) {
                // Calculate the total time spent in seconds for each answer
                minutesSpent += answer.getTimeSpent();
            }
            // Increment the actual value by the total time spent in minutes
            goal.setActualValue(goal.getActualValue() + minutesSpent / 60);
            // Update the goal status based on the actual value
            if (goal.getActualValue() >= goal.getTargetValue()) {
                goal.setStatus(EGoalStatus.COMPLETED);
            }
            goalRepository.save(goal);
        }

        // Update question goals
        for (Goal goal : questionGoals) {
            if (goal.getPart() != null) {
                // For question goals with a specific part, check if the test result contains answers for that part
                // and update the actual value accordingly

                // Check if the goal's part is in the list of parts from the test result
                if (parts.contains(goal.getPart())) {
                    // Count the number of answers for the specific part
                    long questionCount = testResultResponse.getAnswerResponses()
                            .stream()
                            .filter(answer -> answer.getPart().equals(goal.getPart()))
                            .count();
                    goal.setActualValue(goal.getActualValue() + (int) questionCount);
                    // Update the goal status based on the actual value
                    if (goal.getActualValue() >= goal.getTargetValue()) {
                        goal.setStatus(EGoalStatus.COMPLETED);
                    }
                    goalRepository.save(goal);
                }
            }
        }

        // Update part goals
        for (Goal goal : partGoals) {
            if (goal.getPart() != null) {
                // For part goals with a specific part, check if the test result contains answers for that part
                // and update the actual value accordingly

                // Check if the goal's part is in the list of parts from the test result
                if (parts.contains(goal.getPart())) {
                    // Increment the actual value by 1 for each part completed in the test
                    goal.setActualValue(goal.getActualValue() + 1);
                    // Update the goal status based on the actual value
                    if (goal.getActualValue() >= goal.getTargetValue()) {
                        goal.setStatus(EGoalStatus.COMPLETED);
                    }
                    goalRepository.save(goal);
                }
            }
        }

        // Update test goals
        for (Goal goal : testGoals) {
            // For test goals, increment the actual value by 1 for each test completed
            goal.setActualValue(goal.getActualValue() + 1);
            // Update the goal status based on the actual value
            if (goal.getActualValue() >= goal.getTargetValue()) {
                goal.setStatus(EGoalStatus.COMPLETED);
            }
            goalRepository.save(goal);
        }
    }

    private LocalDate getGoalDateByType(EGoalType type) {
        // Get the current date
        LocalDate today = LocalDate.now();
        // Determine the goal date based on the type of goal
        if (EGoalType.DAILY.equals(type)) {
            return today;
        } else if (EGoalType.WEEKLY.equals(type)) {
            return today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        } else {
            return today;
        }
    }

    private void duplicateGoal(Goal goal, LocalDate newDate) {
        // Create a new goal with the same properties as the existing goal
        Goal newGoal = new Goal();
        newGoal.setTitle(goal.getTitle());
        newGoal.setType(goal.getType());
        newGoal.setGoalDate(newDate);
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

    private void updateGoalStatusByActualValue(Goal goal) {
        // Update the status of the goal based on the actual value and target value
        if (goal.getActualValue() >= goal.getTargetValue()) {
            goal.setStatus(EGoalStatus.COMPLETED);
        } else {
            goal.setStatus(EGoalStatus.IN_PROGRESS);
        }
        goalRepository.save(goal);
    }
}
