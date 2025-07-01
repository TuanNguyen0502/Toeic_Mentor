package intern.nhhtuan.toeic_mentor.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationResponse {
    private Long id;
    private String userEmail;
    private String title;
    private boolean isRead;
    private String createdAt;
}
