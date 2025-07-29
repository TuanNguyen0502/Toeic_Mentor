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
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            achievementMap.put(achievement.getStreakMilestone().getDayTarget(), achievement.getAchievedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
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

    @Async
    @Override
    public void updateCurrentStreak(String email) {
        StudyStreak studyStreak = studyStreakRepository.findByUser_Email(email)
                .orElseThrow(() -> new IllegalStateException("Study streak not found for user: " + email));

        LocalDateTime now = LocalDateTime.now();
        if (studyStreak.getLastStudyDate() == null || !studyStreak.getLastStudyDate().toLocalDate().equals(now.toLocalDate())) {
            studyStreak.setLastStudyDate(now);
            studyStreak.setCurrentStreak(studyStreak.getCurrentStreak() + 1);
            if (studyStreak.getCurrentStreak() > studyStreak.getMaxStreak()) {
                studyStreak.setMaxStreak(studyStreak.getCurrentStreak());
            }
            studyStreakRepository.save(studyStreak);
        }

        // Update streak history
        StreakHistory streakHistory = streakHistoryRepository.findFirstByUser_EmailOrderByStartStreakDesc(email);
        if (streakHistory == null || streakHistory.getEndStreak().toLocalDate().isBefore(now.toLocalDate())) {
            // Create a new streak history entry if no current streak history exists or if the last entry is from a previous day
            streakHistory = new StreakHistory();
            streakHistory.setUser(studyStreak.getUser());
            streakHistory.setStartStreak(now);
            streakHistoryRepository.save(streakHistory);
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Ho_Chi_Minh")
    public void lostCurrentStreak() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);

        // Reset current streak if the last study date is not yesterday
        List<StudyStreak> outdatedStreaks = studyStreakRepository.findAllOutdatedStreaks(yesterday);
        for (StudyStreak studyStreak : outdatedStreaks) {
            studyStreak.setCurrentStreak(0);
            studyStreakRepository.save(studyStreak);

            // Reset streak history
            StreakHistory streakHistory = streakHistoryRepository
                    .findFirstByUser_EmailOrderByStartStreakDesc(studyStreak.getUser().getEmail());

            if (streakHistory != null && streakHistory.getEndStreak() == null) {
                // If the streak history exists and has no end date, set the end date to yesterday
                streakHistory.setEndStreak(LocalDateTime.now().minusDays(1));
                streakHistoryRepository.save(streakHistory);
            }
        }
    }
}
