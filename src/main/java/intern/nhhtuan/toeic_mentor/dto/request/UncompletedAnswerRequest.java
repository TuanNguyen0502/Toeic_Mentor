package intern.nhhtuan.toeic_mentor.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import intern.nhhtuan.toeic_mentor.dto.response.TestResultResponse;
import lombok.*;

import java.util.List;

@Builder
@Data
public class UncompletedAnswerRequest {
    private Long questionId;
    private String userAnswer;
    private Integer part;
    private List<TestResultResponse.OptionResponse> options;
    private int timeSpent; // Time spent on the question in seconds
    @JsonProperty("isCorrect")
    private boolean isCorrect;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OptionResponse {
        private String key;
        private String value;
    }
}
