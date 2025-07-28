package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.response.UserStatisticResponse;
import intern.nhhtuan.toeic_mentor.entity.Answer;
import intern.nhhtuan.toeic_mentor.entity.Test;
import intern.nhhtuan.toeic_mentor.entity.User;
import intern.nhhtuan.toeic_mentor.entity.UserStatistic;
import intern.nhhtuan.toeic_mentor.repository.TestRepository;
import intern.nhhtuan.toeic_mentor.repository.UserRepository;
import intern.nhhtuan.toeic_mentor.repository.UserStatisticRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.IUserStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserStatisticServiceImpl implements IUserStatisticService {
    private final UserStatisticRepository userStatisticRepository;
    private final UserRepository userRepository;
    private final TestRepository testRepository;

    @Override
    public List<UserStatisticResponse> getAllUserStatistics(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Fetch all statistics for the user
        List<UserStatistic> userStatistics = userStatisticRepository.findAllByUser(user);
        if (userStatistics.isEmpty()) {
            // If no statistics found, return an empty list
            return List.of();
        }

        // Map UserStatistic to UserStatisticResponse
        return userStatistics.stream()
                .map(statistic -> UserStatisticResponse.builder()
                        .id(statistic.getId())
                        .estimatedScore(statistic.getEstimatedScore())
                        .minEstimatedScore(statistic.getEstimatedScore() - statistic.getScoreInterval())
                        .maxEstimatedScore(statistic.getEstimatedScore() + statistic.getScoreInterval())
                        .totalAnswers(statistic.getTotalAnswers())
                        .totalCorrectAnswers(statistic.getCorrectAnswers())
                        .accuracy(statistic.getAccuracy())
                        .createdAt(statistic.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                        .build())
                .toList();
    }

    @Override
    public UserStatisticResponse getLatestUserStatistic(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Fetch the latest statistic for the user
        UserStatistic userStatistic = userStatisticRepository.findTopByUserOrderByCreatedAtDesc(user);
        if (userStatistic == null) {
            // If no statistic found, return default response
            return UserStatisticResponse.builder()
                    .estimatedScore(0)
                    .minEstimatedScore(0)
                    .maxEstimatedScore(0)
                    .totalAnswers(0)
                    .totalCorrectAnswers(0)
                    .accuracy(0)
                    .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                    .build();
        }

        return UserStatisticResponse.builder()
                .estimatedScore(userStatistic.getEstimatedScore())
                .minEstimatedScore(userStatistic.getEstimatedScore() - userStatistic.getScoreInterval())
                .maxEstimatedScore(userStatistic.getEstimatedScore() + userStatistic.getScoreInterval())
                .totalAnswers(userStatistic.getTotalAnswers())
                .totalCorrectAnswers(userStatistic.getCorrectAnswers())
                .accuracy(userStatistic.getAccuracy())
                .createdAt(userStatistic.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .build();
    }

    @Override
    public UserStatisticResponse calculateEstimatedScore(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Calculate the estimated score based on user's tests and answers
        int totalCorrectAnswers = 0;
        int totalAnswers = 0;

        // Fetch all tests taken by the user
        List<Test> tests = testRepository.findAllByUser(user);
        for (Test test : tests) {
            // Count correct answers for each test
            if (test.getAnswers() == null || test.getAnswers().isEmpty()) {
                continue; // Skip tests with no answers
            }
            // Count correct answers and total answers
            totalCorrectAnswers += (int) test.getAnswers().stream()
                    .filter(Answer::isCorrect)
                    .count();
            totalAnswers += test.getAnswers().size();
        }
        if (totalAnswers == 0 || totalCorrectAnswers == 0) {
            // If no answers or no correct answers, return default response
            return UserStatisticResponse.builder()
                    .estimatedScore(0)
                    .minEstimatedScore(0)
                    .maxEstimatedScore(0)
                    .totalAnswers(0)
                    .totalCorrectAnswers(0)
                    .accuracy(0)
                    .build();
        }

        // Calculate accuracy as a percentage
        double accuracy = (double) totalCorrectAnswers / (float) totalAnswers;
        double standardError = Math.sqrt(accuracy * (1 - accuracy) / totalAnswers);

        // Calculate the estimated score and its range
        int estimatedScore = Math.round(990 * (float) accuracy);
        if (estimatedScore % 5 != 0) {
            estimatedScore = (estimatedScore / 5) * 5; // Round down to nearest multiple of 5
        }

        // Calculate the confidence interval for the estimated score
        // Using a 95% confidence level, the z-score is approximately 1.96
        double interval = 1.96 * (990 * standardError);
        if (interval < 0) {
            interval = 0; // Ensure interval is not negative
        } else if (interval > estimatedScore) {
            interval = estimatedScore; // Cap the interval to the estimated score
        }

        int min = (int) Math.max(0, estimatedScore - interval);
        int max = (int) Math.min(990, estimatedScore + interval);

        // Save the user statistic
        UserStatistic userStatistic = new UserStatistic();
        userStatistic.setUser(user);
        userStatistic.setEstimatedScore(estimatedScore);
        userStatistic.setScoreInterval((int) interval);
        userStatistic.setAccuracy((int) (accuracy * 100)); // Convert to percentage
        userStatistic.setTotalAnswers(totalAnswers);
        userStatistic.setCorrectAnswers(totalCorrectAnswers);
        userStatisticRepository.save(userStatistic);

        return UserStatisticResponse.builder()
                .estimatedScore(estimatedScore)
                .minEstimatedScore(min)
                .maxEstimatedScore(max)
                .totalAnswers(totalAnswers)
                .totalCorrectAnswers(totalCorrectAnswers)
                .accuracy((int) (accuracy * 100)) // Convert to percentage
                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .build();
    }

    @Override
    public boolean deleteUserStatisticById(Long id) {
        if (userStatisticRepository.existsById(id)) {
            userStatisticRepository.deleteById(id);
            return true; // Deletion successful
        } else {
            return false; // Statistic not found
        }
    }
}
