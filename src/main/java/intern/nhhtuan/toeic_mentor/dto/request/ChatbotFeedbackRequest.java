package intern.nhhtuan.toeic_mentor.dto.request;

import intern.nhhtuan.toeic_mentor.entity.enums.EChatbotFeedback;
import lombok.Data;

@Data
public class ChatbotFeedbackRequest {
    private Long chatbotResponseId;
    private EChatbotFeedback feedback;
}
