package intern.nhhtuan.toeic_mentor.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
public class TestResultResponse {
    private Long testId;
    private int score;
    private int correctPercent;
    private List<AnswerResponse> answerResponses;
    private String recommendations;
    private String performance;
    private List<String> referenceUrls;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AnswerResponse {
        private Long id;
        private String questionText;
        private String correctAnswer;
        private String userAnswer;
        private Integer part;
        private List<OptionResponse> options;
        private List<String> tags;
        private int timeSpent; // Time spent on the question in seconds
        @JsonProperty("isCorrect")
        private boolean isCorrect;
        private List<String> answerExplanation;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OptionResponse {
        private String key;
        private String value;
    }
}
