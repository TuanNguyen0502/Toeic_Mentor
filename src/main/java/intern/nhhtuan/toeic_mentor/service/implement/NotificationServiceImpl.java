package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.response.NotificationDetailResponse;
import intern.nhhtuan.toeic_mentor.dto.response.NotificationResponse;
import intern.nhhtuan.toeic_mentor.entity.Notification;
import intern.nhhtuan.toeic_mentor.entity.Report;
import intern.nhhtuan.toeic_mentor.entity.User;
import intern.nhhtuan.toeic_mentor.entity.enums.ENotificationType;
import intern.nhhtuan.toeic_mentor.entity.enums.ERole;
import intern.nhhtuan.toeic_mentor.repository.NotificationRepository;
import intern.nhhtuan.toeic_mentor.repository.UserRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.INotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements INotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void createReportNotifications(Report report) {
        List<User> admins = userRepository.findAllByRole_Name(ERole.ROLE_ADMIN);

        if (admins.isEmpty()) return;

        List<Notification> notifications = admins.stream().map(admin -> {
            return Notification.builder()
                    .receiver(admin)
                    .report(report)
                    .type(ENotificationType.ERROR_REPORT)
                    .title("New Report for Question #" + report.getQuestion().getId())
                    .message("User " + report.getUser().getFullName() + " submitted a new report.")
                    .build();
        }).toList();

        notificationRepository.saveAll(notifications);
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
                        .userEmail(email)
                        .title(notification.getTitle())
                        .isRead(notification.isRead())
                        .createdAt(notification.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                        .build())
                .toList();
    }

    @Override
    public NotificationDetailResponse getNotificationDetailResponse(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        return NotificationDetailResponse.builder()
                .notificationId(notification.getId())
                .userEmail(notification.getReceiver().getEmail())
                .urlToReportDetail("/reports/{" + notification.getReport().getId() + "}")
                .title(notification.getTitle())
                .message(notification.getMessage())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
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
