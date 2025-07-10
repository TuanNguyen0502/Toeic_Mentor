package intern.nhhtuan.toeic_mentor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {
    private Long id;
    private String questionText;
    private String correctAnswer;
    private String answerExplanation;
    private String userAnswer;
    private String passage;
    private List<String> passageImageUrls;
    private Integer part;
    private List<OptionResponse> options;
    private List<String> tags;
    private Integer difficulty; // Difficulty level of the question

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OptionResponse {
        private String key;
        private String value;
    }
}
