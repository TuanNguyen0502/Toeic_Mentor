package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.response.StudyStreakDetailResponse;

public interface IStudyStreakService {
    int getCurrentStreak(String email);

    StudyStreakDetailResponse getStudyStreakDetail(String email);
}
