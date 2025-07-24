package intern.nhhtuan.toeic_mentor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ChatbotRatingDetailResponse {
    private Long id;
    private String userEmail;
    private String messageId;
    private String content;
    private String conversationTitle;
    private String chatbotResponseCreatedAt;
    private String rating;
    private String ratedAt;
}
