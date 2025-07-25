package intern.nhhtuan.toeic_mentor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatbotRatingResponse {
    private Long id;
    private String userEmail;
    private String conversationName;
    private String rating;
    private String ratedAt;
}
