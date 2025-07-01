package intern.nhhtuan.toeic_mentor.controller.admin;

import intern.nhhtuan.toeic_mentor.dto.response.NotificationResponse;
import intern.nhhtuan.toeic_mentor.service.interfaces.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

import java.util.List;

@ControllerAdvice(basePackages = "intern.nhhtuan.toeic_mentor.controller.admin")
@RequiredArgsConstructor
public class AdminGlobalModelAdvice {
    private final INotificationService notificationService;

    @ModelAttribute
    public void addNotificationsToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            String email = authentication.getName();
            List<NotificationResponse> notifications = notificationService.getNotificationResponses(email, null, 10);
            model.addAttribute("notifications", notifications);
        }
    }
}

