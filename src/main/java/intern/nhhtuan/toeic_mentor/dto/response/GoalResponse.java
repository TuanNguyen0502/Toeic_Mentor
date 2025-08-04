package intern.nhhtuan.toeic_mentor.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class GoalResponse {
    private Long id;
    private String title;
    private String type;
    private LocalDate goalDate;
    private int targetValue;
    private int actualValue;
    private String unit;
    private int part;
    private String status;
}
