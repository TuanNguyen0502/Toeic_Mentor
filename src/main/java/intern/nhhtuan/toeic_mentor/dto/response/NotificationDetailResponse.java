package intern.nhhtuan.toeic_mentor.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NotificationDetailResponse {
    private Long notificationId;
    private String urlToReportDetail;
    private String title;
    private String message;
    private boolean isRead;
    private String createdAt;
}
