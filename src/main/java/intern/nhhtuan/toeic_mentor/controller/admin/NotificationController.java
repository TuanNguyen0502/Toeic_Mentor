package intern.nhhtuan.toeic_mentor.controller.admin;

import intern.nhhtuan.toeic_mentor.dto.response.NotificationDetailResponse;
import intern.nhhtuan.toeic_mentor.dto.response.NotificationResponse;
import intern.nhhtuan.toeic_mentor.service.interfaces.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final INotificationService notificationService;

    @GetMapping
    public List<NotificationResponse> getNotifications(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime before,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
//        GET /api/notifications?userId=123&pageSize=10
//        Server trả 10 notification mới nhất, sắp xếp giảm dần theo createdAt.
//        Khi user scroll để load thêm:
//        Lấy createdAt của notification cuối cùng.
//        Gửi request:
//        GET /api/notifications?userId=123&before=2024-06-30T15:00:00&pageSize=10
        Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return notificationService.getNotificationResponses(email, before, pageSize);
    }

    @GetMapping("/count")
    public int countUnreadNotifications() {
        Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return notificationService.countUnreadNotifications(email);
    }

    @GetMapping("/{id}")
    public NotificationDetailResponse getNotificationDetail(@PathVariable Long id) {
        return notificationService.getNotificationDetailResponse(id);
    }

    @PostMapping("/{id}/read")
    public void markNotificationAsRead(@PathVariable Long id) {
        notificationService.markIsRead(id);
    }

    @PostMapping("/read-all")
    public void markAllNotificationsAsRead() {
        Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        notificationService.markAllAsRead(email);
    }
}
