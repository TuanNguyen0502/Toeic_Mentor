package intern.nhhtuan.toeic_mentor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatbotFeedbackResponse {
    private Long id;
    private String userEmail;
    private String conversationName;
    private String feedbackTypes;
    private String createdAt;
}
