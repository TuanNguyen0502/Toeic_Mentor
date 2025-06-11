package intern.nhhtuan.toeic_mentor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class QuestionDTO {
    @JsonProperty("question_number")
    private Integer questionNumber;

    @JsonProperty("question_text")
    private String questionText;

    private Map<String, String> options;

    @JsonProperty("correct_answer")
    private String correctAnswer;

    private List<String> tags;

    private String passage;

    @JsonProperty("passage_image_url")
    private String passageImageUrl;

    private Integer part;
}
