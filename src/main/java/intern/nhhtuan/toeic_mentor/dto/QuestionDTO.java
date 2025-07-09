package intern.nhhtuan.toeic_mentor.dto;

import intern.nhhtuan.toeic_mentor.entity.enums.EQuestionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class QuestionDTO {
    private Long id;
    private Integer part;
    private Integer questionNumber;
    private List<String> passageImageUrls;
    private String passage;
    private String questionText;
    private Map<String, String> options;
    private String correctAnswer;
    private List<String> tags;
    private Long sectionId;
    private EQuestionStatus status;
    private String answerExplanation;
}
