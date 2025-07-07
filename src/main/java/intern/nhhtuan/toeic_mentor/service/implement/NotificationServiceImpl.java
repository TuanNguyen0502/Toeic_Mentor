package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.ReportDetailDTO;
import intern.nhhtuan.toeic_mentor.dto.response.NotificationDetailResponse;
import intern.nhhtuan.toeic_mentor.dto.response.NotificationResponse;
import intern.nhhtuan.toeic_mentor.entity.Notification;
import intern.nhhtuan.toeic_mentor.entity.NotificationType;
import intern.nhhtuan.toeic_mentor.entity.Report;
import intern.nhhtuan.toeic_mentor.entity.User;
import intern.nhhtuan.toeic_mentor.entity.enums.ENotificationTypeAction;
import intern.nhhtuan.toeic_mentor.entity.enums.ERole;
import intern.nhhtuan.toeic_mentor.repository.NotificationRepository;
import intern.nhhtuan.toeic_mentor.repository.NotificationSettingRepository;
import intern.nhhtuan.toeic_mentor.repository.NotificationTypeRepository;
import intern.nhhtuan.toeic_mentor.repository.UserRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.INotificationService;
import intern.nhhtuan.toeic_mentor.util.WebSocketNotifier;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements INotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationTypeRepository notificationTypeRepository;
    private final NotificationSettingRepository notificationSettingRepository;
    private final UserRepository userRepository;
    private final WebSocketNotifier socketNotifier;

    @Override
    @Transactional
    public void createReportNotifications(Report report) {
        List<User> admins = userRepository.findAllByRole_Name(ERole.ROLE_ADMIN);
        if (admins.isEmpty()) return;

        NotificationType type = notificationTypeRepository.findByAction(ENotificationTypeAction.NEW_REPORT);

        List<User> enabledAdmins = admins.stream()
                .filter(admin -> notificationSettingRepository
                        .isEnabled(admin.getId(), ENotificationTypeAction.NEW_REPORT)
                        .orElse(false)
                ).toList();

        if (enabledAdmins.isEmpty()) return;

        List<Notification> notifications = enabledAdmins.stream().map(admin ->
                Notification.builder()
                        .receiver(admin)
                        .report(report)
                        .notificationType(type)
                        .title("New Report for Question #" + report.getQuestion().getId())
                        .message("User " + report.getUser().getFullName() + " submitted a new report.")
                        .build()
        ).toList();

        notificationRepository.saveAll(notifications);

        List<NotificationResponse> responses = notifications.stream()
                .map(notification -> NotificationResponse.builder()
                        .id(notification.getId())
                        .urlToReportDetail("/admin/reports/" + notification.getReport().getId())
                        .title(notification.getTitle())
                        .message(notification.getMessage())
                        .isRead(notification.isRead())
                        .createdAt(notification.getCreatedAt().toString())
                        .build()
                ).toList();

        responses.forEach(response -> {
            Notification notification = notifications.stream()
                    .filter(n -> n.getId().equals(response.getId()))
                    .findFirst()
                    .orElse(null);

            if (notification != null) {
                String email = notification.getReceiver().getEmail();
                socketNotifier.sendToUser(email, "/queue/admin/notifications", response);
            }
        });
    }

    @Override
    public void createResponseUserNotifications(ReportDetailDTO reportDetailDTO) {
        User userReceiver = userRepository.findByEmail(reportDetailDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + reportDetailDTO.getEmail()));

        // check user setting notification
        Optional<Boolean> enableNotification = notificationSettingRepository.isEnabled(userReceiver.getId(), ENotificationTypeAction.COMPLETE_REPORT);

        if (enableNotification.orElse(false)) {
            // User has enabled notification for this action
            Notification notification = Notification.builder()
                    .receiver(userReceiver)
                    .report(Report.builder().id(reportDetailDTO.getId()).build())
                    .notificationType(notificationTypeRepository.findByAction(ENotificationTypeAction.COMPLETE_REPORT))
                    .title("Response to Report #" + reportDetailDTO.getId())
                    .message(reportDetailDTO.getAdmin_response())
                    .build();

            notificationRepository.save(notification);

            // Send WebSocket notification to user
            NotificationResponse notificationResponse = NotificationResponse.builder()
                    .id(notification.getId())
                    .urlToReportDetail("")
                    .title(notification.getTitle())
                    .message(notification.getMessage())
                    .isRead(notification.isRead())
                    .createdAt(notification.getCreatedAt().toString())
                    .build();

            socketNotifier.sendToUser(userReceiver.getEmail(), "/queue/notifications", notificationResponse);
        }
    }

    @Override
    public int countUnreadNotifications(String email) {
        return notificationRepository.findAllByReceiver_Email(email)
                .stream()
                .filter(notification -> !notification.isRead())
                .mapToInt(notification -> 1)
                .sum();
    }

    @Override
    public List<NotificationResponse> getNotificationResponses(String email, LocalDateTime before, int pageSize) {
        // Nếu client chưa gửi cursor thì lấy thời điểm hiện tại
        if (before == null) {
            before = LocalDateTime.now();
        }
        Pageable pageable = PageRequest.of(0, pageSize);
        return notificationRepository.findAllByReceiver_EmailAndCreatedAtBeforeOrderByCreatedAtDesc(email, before, pageable)
                .stream()
                .map(notification -> NotificationResponse.builder()
                        .id(notification.getId())
                        .urlToReportDetail("/admin/reports/{" + notification.getReport().getId().toString() + "}")
                        .title(notification.getTitle())
                        .message(notification.getMessage())
                        .isRead(notification.isRead())
                        .createdAt(notification.getCreatedAt().toString())
                        .build())
                .toList();
    }

    @Override
    public NotificationDetailResponse getNotificationDetailResponse(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        return NotificationDetailResponse.builder()
                .notificationId(notification.getId())
                .urlToReportDetail("/reports/{" + notification.getReport().getId() + "}")
                .title(notification.getTitle())
                .message(notification.getMessage())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt().toString())
                .build();
    }

    @Override
    public boolean markIsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        if (notification.isRead()) {
            return false; // Notification is already read
        }

        notification.setRead(true);
        notificationRepository.save(notification);
        return true; // Successfully marked as read
    }

    @Override
    public boolean markAllAsRead(String email) {
        List<Notification> notifications = notificationRepository.findAllByReceiver_Email(email);
        if (notifications.isEmpty()) {
            return false; // No notifications to mark as read
        }

        notifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(notifications);
        return true; // Successfully marked all as read
    }
}
