package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.request.NotificationSettingRequest;
import intern.nhhtuan.toeic_mentor.dto.response.NotificationSettingResponse;
import intern.nhhtuan.toeic_mentor.entity.NotificationSetting;
import intern.nhhtuan.toeic_mentor.entity.NotificationSettingId;
import intern.nhhtuan.toeic_mentor.entity.NotificationType;
import intern.nhhtuan.toeic_mentor.entity.User;
import intern.nhhtuan.toeic_mentor.repository.NotificationSettingRepository;
import intern.nhhtuan.toeic_mentor.repository.NotificationTypeRepository;
import intern.nhhtuan.toeic_mentor.repository.UserRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.INotificationSettingService;
import intern.nhhtuan.toeic_mentor.service.interfaces.IRoleNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationSettingServiceImpl implements INotificationSettingService {
    private final NotificationSettingRepository notificationSettingRepository;
    private final NotificationTypeRepository notificationTypeRepository;
    private final IRoleNotificationService roleNotificationService;
    private final UserRepository userRepository;

    @Override
    public List<NotificationSettingResponse> getNotificationSettingsByEmail(String email) {
        // Validate the email
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        // Fetch the user by email
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user == null) {
            throw new IllegalArgumentException("User not found with email: " + email);
        }

        // Fetch all notification settings for the user
        List<NotificationType> notificationTypes = roleNotificationService.getNotificationTypesByRole(user.getRole());

        // Prepare the response list
        List<NotificationSettingResponse> responses = new ArrayList<>();
        // Iterate through each notification type and check the user's settings
        for (NotificationType notificationType : notificationTypes) {
            NotificationSetting setting = notificationSettingRepository
                    .findByUser_EmailAndNotificationType_Action(email, notificationType.getAction());
            NotificationSettingResponse response = new NotificationSettingResponse();
            response.setNotificationType(notificationType.getAction());
            response.setDescription(notificationType.getDescription());
            response.setEnabled(setting != null && setting.isEnabled());
            responses.add(response);
        }

        // Convert to response DTOs
        return responses;
    }

    @Async
    @Override
    public void createNewUserNotificationSettings(User user) {
        // Fetch all notification types for the user's role
        List<NotificationType> notificationTypes = roleNotificationService.getNotificationTypesByRole(user.getRole());

        // Create default notification settings for each type
        for (NotificationType notificationType : notificationTypes) {
            NotificationSetting setting = new NotificationSetting();
            setting.setId(new NotificationSettingId(user.getId(), notificationType.getId()));
            setting.setUser(user);
            setting.setNotificationType(notificationType);
            setting.setEnabled(true); // Default to enabled
            notificationSettingRepository.save(setting);
        }
    }

    @Override
    public boolean updateNotificationSettings(String email, List<NotificationSettingRequest> requests) {
        // Validate the request and email
        if (email == null || requests == null || requests.isEmpty()) {
            return false;
        }

        // Process each notification setting
        for (NotificationSettingRequest setting : requests) {
            NotificationSetting notificationSetting = notificationSettingRepository
                    .findByUser_EmailAndNotificationType_Action(email, setting.getNotificationType());
            if (notificationSetting == null) {
                // If the notification setting does not exist, create a new one
                User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
                NotificationType notificationType = notificationTypeRepository.findByAction(setting.getNotificationType());
                notificationSetting = new NotificationSetting();
                notificationSetting.setId(new NotificationSettingId(user.getId(), notificationType.getId()));
                notificationSetting.setUser(user);
                notificationSetting.setNotificationType(notificationType);
                notificationSetting.setEnabled(setting.isEnabled());
                notificationSettingRepository.save(notificationSetting);
            } else {
                // Update the notification setting
                notificationSetting.setEnabled(setting.isEnabled());
                notificationSettingRepository.save(notificationSetting);
            }
        }

        return true;
    }
}
