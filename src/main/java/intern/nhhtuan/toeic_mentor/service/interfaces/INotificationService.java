package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.ReportDetailDTO;
import intern.nhhtuan.toeic_mentor.dto.response.NotificationDetailResponse;
import intern.nhhtuan.toeic_mentor.dto.response.NotificationResponse;
import intern.nhhtuan.toeic_mentor.entity.Report;

import java.time.LocalDateTime;
import java.util.List;

public interface INotificationService {
    void createReportNotifications(Report report);
  
    void createResponseUserNotifications(ReportDetailDTO reportDetailDTO);

    int countUnreadNotifications(String email);

    List<NotificationResponse> getNotificationResponses(String email, LocalDateTime before, int pageSize);

    NotificationDetailResponse getNotificationDetailResponse(Long notificationId);

    boolean markIsRead(Long notificationId);

    boolean markAllAsRead(String email);
}
