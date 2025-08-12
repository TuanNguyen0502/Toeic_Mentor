package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.request.NotificationSettingRequest;
import intern.nhhtuan.toeic_mentor.dto.response.NotificationSettingResponse;
import intern.nhhtuan.toeic_mentor.entity.User;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface INotificationSettingService {
    List<NotificationSettingResponse> getNotificationSettingsByEmail(String email);

    @Async
    void createNewUserNotificationSettings(User user);

    boolean updateNotificationSettings(String email, List<NotificationSettingRequest> requests);
}
