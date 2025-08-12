package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.response.StudyStreakDetailResponse;
import intern.nhhtuan.toeic_mentor.entity.*;
import intern.nhhtuan.toeic_mentor.repository.StreakAchievementRepository;
import intern.nhhtuan.toeic_mentor.repository.StreakHistoryRepository;
import intern.nhhtuan.toeic_mentor.repository.StreakMilestoneRepository;
import intern.nhhtuan.toeic_mentor.repository.StudyStreakRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.INotificationService;
import intern.nhhtuan.toeic_mentor.service.interfaces.IStudyStreakService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyStreakServiceImpl implements IStudyStreakService {
    private final StudyStreakRepository studyStreakRepository;
    private final StreakAchievementRepository streakAchievementRepository;
    private final StreakHistoryRepository streakHistoryRepository;
    private final StreakMilestoneRepository streakMilestoneRepository;
    private final INotificationService notificationService;

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

        HashMap<String, String> achievementMap = new HashMap<>();
        for (StreakAchievement achievement : streakAchievements) {
            achievementMap.put(
                    achievement.getStreakMilestone().getTitle() + " (" + achievement.getStreakMilestone().getDayTarget() + " days)",
                    achievement.getAchievedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );
        }
        HashMap<LocalDate, LocalDate> historyMap = new HashMap<>();
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
        LocalDate now = LocalDate.now();
        // Check if the last study date is today
        LocalDate lastStudyDate = studyStreakRepository.findLastStudyDateByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Last study date not found for user: " + email));
        if (lastStudyDate.equals(now)) {
            // If the last study date is today, no need to update
            return;
        }

        // Update current streak for the user
        StudyStreak studyStreak = studyStreakRepository.findByUser_Email(email)
                .orElseThrow(() -> new IllegalStateException("Study streak not found for user: " + email));

        if (studyStreak.getLastStudyDate() == null || !studyStreak.getLastStudyDate().equals(now)) {
            studyStreak.setLastStudyDate(now);
            studyStreak.setCurrentStreak(studyStreak.getCurrentStreak() + 1);
            if (studyStreak.getCurrentStreak() > studyStreak.getMaxStreak()) {
                studyStreak.setMaxStreak(studyStreak.getCurrentStreak());
            }
            studyStreakRepository.save(studyStreak);
        }

        // Update streak history
        StreakHistory streakHistory = streakHistoryRepository.findFirstByUser_EmailOrderByStartStreakDesc(email);
        if (streakHistory == null || streakHistory.getEndStreak().isBefore(now)) {
            // Create a new streak history entry if no current streak history exists or if the last entry is from a previous day
            streakHistory = new StreakHistory();
            streakHistory.setUser(studyStreak.getUser());
            streakHistory.setStartStreak(now);
            streakHistory.setEndStreak(now);
            streakHistoryRepository.save(streakHistory);
        }

        // Check for streak achievements
        StreakMilestone nextStreakMilestone = streakMilestoneRepository.findByDayTarget(studyStreak.getCurrentStreak());
        if (nextStreakMilestone != null &&
                !streakAchievementRepository.existsByUser_EmailAndStreakMilestone_DayTarget(email, nextStreakMilestone.getDayTarget())) {
            // Create a new streak achievement if the current streak meets or exceeds the milestone
            StreakAchievement streakAchievement = new StreakAchievement();
            streakAchievement.setUser(studyStreak.getUser());
            streakAchievement.setStreakMilestone(nextStreakMilestone);
            streakAchievement.setAchievedAt(now);
            streakAchievementRepository.save(streakAchievement);

            // Notify the user about the achievement
            notificationService.createUserStreakAchievementNotifications(email, nextStreakMilestone.getTitle(), nextStreakMilestone.getDayTarget());
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Ho_Chi_Minh")
    public void lostCurrentStreak() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

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
                streakHistory.setEndStreak(LocalDate.now().minusDays(1));
                streakHistoryRepository.save(streakHistory);
            }
        }
    }

    @Async
    @Override
    public void createNewUserStudyStreak(User user) {
        // Kiểm tra xem người dùng đã có StudyStreak chưa
        if (studyStreakRepository.existsByUser(user)) {
            return; // Người dùng đã có StudyStreak, không cần tạo mới
        }

        // Tạo StudyStreak mới cho người dùng
        StudyStreak studyStreak = new StudyStreak();
        studyStreak.setCurrentStreak(1);
        studyStreak.setMaxStreak(1);
        studyStreak.setLastStudyDate(LocalDate.now());
        studyStreak.setUser(user);
        studyStreakRepository.save(studyStreak);

        // Tạo StreakHistory mới cho người dùng
        StreakHistory streakHistory = new StreakHistory();
        streakHistory.setStartStreak(LocalDate.now());
        streakHistory.setUser(user);
        streakHistoryRepository.save(streakHistory);
    }
}
