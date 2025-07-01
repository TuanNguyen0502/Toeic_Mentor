package intern.nhhtuan.toeic_mentor.dto;

import intern.nhhtuan.toeic_mentor.entity.enums.EQuestionStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder
public class QuestionUpdateDTO {
    private Long id;

    @Min(value = 1, message = "Part must be greater than or equal to 1")
    @Max(value = 7, message = "Part must be less than or equal to 7")
    private Integer part;

    private Integer number;

    private List<String> imageUrls;

    private String passage;

    @NotBlank(message = "Content cannot be blank")
    private String content;

    private Map<String, String> options;

    @NotBlank(message = "Correct Answer cannot be blank")
    @Pattern(regexp = "^[A-D]$", message = "Correct Answer must be A, B, C, or D")
    private String correctAnswer;

    private List<String> tags;

    private EQuestionStatus status;

    @NotBlank(message = "Explanation cannot be blank")
    private String answerExplanation;
}
