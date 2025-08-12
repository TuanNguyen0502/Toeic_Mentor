package intern.nhhtuan.toeic_mentor.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;

@Data
@Builder
public class StudyStreakDetailResponse {
    private int currentStreak;
    private int maxStreak;
    private String lastStudyDate;
    private HashMap<String, String> achievements;
    private HashMap<LocalDate, LocalDate> histories;
}
