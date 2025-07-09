package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.request.NotificationSettingRequest;
import intern.nhhtuan.toeic_mentor.dto.response.NotificationSettingResponse;

import java.util.List;

public interface INotificationSettingService {
    List<NotificationSettingResponse> getNotificationSettingsByEmail(String email);

    boolean updateNotificationSettings(String email, List<NotificationSettingRequest> requests);
}
