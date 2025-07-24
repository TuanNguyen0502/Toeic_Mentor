package intern.nhhtuan.toeic_mentor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatbotFeedbackDetailResponse {
    private Long id;
    private String userEmail;
    private Long chatbotResponseId;
    private String conversationName;
    private String chatbotResponse;
    private String chatbotResponseCreatedAt;
    private String feedbackTypes;
    private String createdAt;
}
