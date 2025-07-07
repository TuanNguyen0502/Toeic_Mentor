package intern.nhhtuan.toeic_mentor.dto.request;

import intern.nhhtuan.toeic_mentor.entity.enums.ENotificationTypeAction;
import lombok.Data;

@Data
public class NotificationSettingRequest {
    private ENotificationTypeAction notificationType;
    private boolean enabled;
}
