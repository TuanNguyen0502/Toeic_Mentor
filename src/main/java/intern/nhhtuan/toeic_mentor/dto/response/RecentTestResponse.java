package intern.nhhtuan.toeic_mentor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecentTestResponse {
    private Long testId;
    private String createdAt;
    private Integer totalAnswers;
    private Integer score;
}
