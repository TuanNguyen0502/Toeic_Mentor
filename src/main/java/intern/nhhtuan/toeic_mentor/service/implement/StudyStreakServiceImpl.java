package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.response.StudyStreakDetailResponse;
import intern.nhhtuan.toeic_mentor.entity.StreakAchievement;
import intern.nhhtuan.toeic_mentor.entity.StreakHistory;
import intern.nhhtuan.toeic_mentor.entity.StudyStreak;
import intern.nhhtuan.toeic_mentor.repository.StreakAchievementRepository;
import intern.nhhtuan.toeic_mentor.repository.StreakHistoryRepository;
import intern.nhhtuan.toeic_mentor.repository.StudyStreakRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.IStudyStreakService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyStreakServiceImpl implements IStudyStreakService {
    private final StudyStreakRepository studyStreakRepository;
    private final StreakAchievementRepository streakAchievementRepository;
    private final StreakHistoryRepository streakHistoryRepository;

    @Override
    public int getCurrentStreak(String email) {
        return studyStreakRepository.getCurrentStreak(email);
    }

    @Override
    public StudyStreakDetailResponse getStudyStreakDetail(String email) {
        StudyStreak studyStreak = studyStreakRepository.findByUser_Email(email)
                .orElseThrow(() -> new IllegalStateException("Study streak not found for user: " + email));
        List<StreakAchievement> streakAchievements = streakAchievementRepository.findByUser_Email(email);
        List<StreakHistory> streakHistories = streakHistoryRepository.findByUser_Email(email);

        HashMap<Integer, String> achievementMap = new HashMap<>();
        for (StreakAchievement achievement : streakAchievements) {
            achievementMap.put(achievement.getMilestone(), achievement.getAchievedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        HashMap<LocalDateTime, LocalDateTime> historyMap = new HashMap<>();
        for (StreakHistory history : streakHistories) {
            historyMap.put(history.getStartStreak(), history.getEndStreak());
        }

        return StudyStreakDetailResponse.builder()
                .currentStreak(studyStreak.getCurrentStreak())
                .maxStreak(studyStreak.getMaxStreak())
                .lastStudyDate(studyStreak.getLastStudyDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .achievements(achievementMap)
                .histories(historyMap)
                .build();
    }
}
