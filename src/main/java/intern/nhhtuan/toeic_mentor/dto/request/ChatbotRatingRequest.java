package intern.nhhtuan.toeic_mentor.dto.request;

import intern.nhhtuan.toeic_mentor.entity.enums.EChatbotRating;
import lombok.Data;

@Data
public class ChatbotRatingRequest {
    private String messageId;
    private EChatbotRating rating;
}
