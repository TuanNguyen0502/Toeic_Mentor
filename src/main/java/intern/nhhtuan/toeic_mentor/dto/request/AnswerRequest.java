package intern.nhhtuan.toeic_mentor.dto.request;

import intern.nhhtuan.toeic_mentor.dto.response.QuestionResponse;
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
    private String userAnswer;
    private Integer part;
    private List<QuestionResponse.OptionResponse> options;
    private List<String> tags;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OptionResponse {
        private String key;
        private String value;
    }
}
