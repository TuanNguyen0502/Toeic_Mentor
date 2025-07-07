package intern.nhhtuan.toeic_mentor.dto.response;

import intern.nhhtuan.toeic_mentor.entity.enums.ENotificationTypeAction;
import lombok.Data;

@Data
public class NotificationSettingResponse {
    private ENotificationTypeAction notificationType;
    private boolean enabled;
}
