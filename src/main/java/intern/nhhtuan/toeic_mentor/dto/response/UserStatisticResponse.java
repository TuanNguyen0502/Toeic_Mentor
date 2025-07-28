package intern.nhhtuan.toeic_mentor.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStatisticResponse {
    private int estimatedScore;
    private int minEstimatedScore;
    private int maxEstimatedScore;
    private int totalAnswers;
    private int totalCorrectAnswers;
    private int accuracy;
    private String createdAt;
}
