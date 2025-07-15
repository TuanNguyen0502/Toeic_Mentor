package intern.nhhtuan.toeic_mentor.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerRequest {
    private Long id;
    private String questionText;
    private String correctAnswer;
    private String answerExplanation;
    private String userAnswer;
    private Integer part;
    private List<OptionResponse> options;
    private List<String> tags;
    private int timeSpent; // Time spent on the question in seconds
    private Integer difficulty; // Difficulty level of the question

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OptionResponse {
        private String key;
        private String value;
    }
}
