package intern.nhhtuan.toeic_mentor.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class QuestionRequest {
    private Integer questionNumber;

    private String questionText;

    private Map<String, String> options;

    private String correctAnswer;

    private List<String> tags;

    private String passage;

    private List<String> passageImageUrls;

    private Integer part;
}
