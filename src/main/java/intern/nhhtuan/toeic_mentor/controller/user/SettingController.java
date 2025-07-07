package intern.nhhtuan.toeic_mentor.controller.user;

import intern.nhhtuan.toeic_mentor.dto.request.NotificationSettingRequest;
import intern.nhhtuan.toeic_mentor.service.interfaces.INotificationSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("UserSettingController")
@RequestMapping("/settings")
@RequiredArgsConstructor
public class SettingController {
    private final INotificationSettingService notificationSettingService;

    @PostMapping("/notifications")
    public String updateNotificationSettings(@RequestBody List<NotificationSettingRequest> notificationSettingRequests) {
        if (notificationSettingRequests == null || notificationSettingRequests.isEmpty()) {
            return "No notification settings provided";
        }

        Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        boolean result = notificationSettingService.updateNotificationSettings(email, notificationSettingRequests);
        if (!result) {
            return "Failed to update notification settings";
        }
        // Logic to update notification settings
        return "Notification settings updated successfully";
    }
}
