package intern.nhhtuan.toeic_mentor.dto.request;

import intern.nhhtuan.toeic_mentor.entity.enums.EGoalType;
import intern.nhhtuan.toeic_mentor.entity.enums.EGoalUnit;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoalCreateRequest {
    @NotNull(message = "Title cannot be null")
    @NotBlank(message = "Title cannot be blank")
    private String title;

    private EGoalType type;

    @NotNull(message = "Target value cannot be null")
    @Min(value = 1, message = "Target value must be at least 1")
    private Integer targetValue;

    private EGoalUnit unit;

    private Integer part;
}
