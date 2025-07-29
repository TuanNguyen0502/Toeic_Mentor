package intern.nhhtuan.toeic_mentor.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestStatisticResponse {
    private Integer totalTests;
    private Integer averageScore;
    private Integer highestScore;
    private Integer totalTimeSpent;
}