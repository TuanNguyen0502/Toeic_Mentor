package intern.nhhtuan.toeic_mentor.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SectionQuestionResponse {
    private Long id;
    private Integer part;
    private String status;
    private String text;
    private String correctAnswer;
    private String tags;
}
