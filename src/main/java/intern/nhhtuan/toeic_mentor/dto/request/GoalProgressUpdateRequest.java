package intern.nhhtuan.toeic_mentor.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoalProgressUpdateRequest {
    private Integer part;
    private int timeSpent;
}
