package intern.nhhtuan.toeic_mentor.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
public class TestHistoryDetailResponse {
    private Long testId;
    private int score;
    private int correctPercent;
    private String recommendations;
    private String performance;
    private List<String> referenceUrls;
    private String createdAt;
    private List<AnswerResponse> answerResponses;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AnswerResponse {
        private Long answerId;
        private String userAnswer;
        @JsonProperty("isCorrect")
        private boolean isCorrect;
        private List<String> optionExplanation;
        private int timeSpent; // Time spent on the question in seconds

        private Long questionId;
        private String correctAnswer;
        private String passage;
        private String questionText;
        private int part;
        private int difficulty;
        private List<String> tags;
        private List<String> questionImages;
        private List<OptionResponse> options;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OptionResponse {
        private String key;
        private String value;
    }
}
