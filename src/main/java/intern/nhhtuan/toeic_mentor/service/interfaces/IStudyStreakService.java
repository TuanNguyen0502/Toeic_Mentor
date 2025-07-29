package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.response.StudyStreakDetailResponse;
import org.springframework.scheduling.annotation.Async;

public interface IStudyStreakService {
    int getCurrentStreak(String email);

    StudyStreakDetailResponse getStudyStreakDetail(String email);

    @Async
    void updateCurrentStreak(String email);
}
