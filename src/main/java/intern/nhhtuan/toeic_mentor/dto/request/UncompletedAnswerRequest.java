package intern.nhhtuan.toeic_mentor.dto.request;

import lombok.*;

@Builder
@Data
public class UncompletedAnswerRequest {
    private Long questionId;
    private String userAnswer;
    private Integer part;
    private int timeSpent; // Time spent on the question in seconds
}
