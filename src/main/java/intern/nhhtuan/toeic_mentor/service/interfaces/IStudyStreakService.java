package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.response.StudyStreakDetailResponse;
import intern.nhhtuan.toeic_mentor.entity.User;
import org.springframework.scheduling.annotation.Async;

public interface IStudyStreakService {
    int getCurrentStreak(String email);

    StudyStreakDetailResponse getStudyStreakDetail(String email);

    @Async
    void updateCurrentStreak(String email);

    @Async
    void createNewUserStudyStreak(User user);
}
