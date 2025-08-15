package intern.nhhtuan.toeic_mentor.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestHistoryResponse {
    private Long id;
    private int correctAnswers;
    private int totalQuestions;
    private String parts;
    private int timeSpent;
    private String completedAt;
    private String createdAt;
}
