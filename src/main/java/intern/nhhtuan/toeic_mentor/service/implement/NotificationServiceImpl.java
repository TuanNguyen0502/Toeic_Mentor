package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.ReportDetailDTO;
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
import org.springframework.stereotype.Service;

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
    public void createResponseUserNotifications(ReportDetailDTO reportDetailDTO) {
        User userReceiver = userRepository.findByEmail(reportDetailDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + reportDetailDTO.getEmail()));

        Notification notification = Notification.builder()
                .receiver(userReceiver)
                .report(Report.builder().id(reportDetailDTO.getId()).build())
                .type(ENotificationType.USER)
                .title("Response to Report #" + reportDetailDTO.getId())
                .message(reportDetailDTO.getAdmin_response())
                .build();

        notificationRepository.save(notification);
    }
}
