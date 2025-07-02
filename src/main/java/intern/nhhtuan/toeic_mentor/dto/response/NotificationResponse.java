package intern.nhhtuan.toeic_mentor.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationResponse {
    private Long id;
    private String title;
    @JsonProperty("isRead")
    private boolean isRead;
    private String createdAt;
}
