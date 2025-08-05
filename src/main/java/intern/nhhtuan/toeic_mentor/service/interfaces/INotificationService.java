package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.ReportDetailDTO;
import intern.nhhtuan.toeic_mentor.dto.response.NotificationDetailResponse;
import intern.nhhtuan.toeic_mentor.dto.response.NotificationResponse;
import intern.nhhtuan.toeic_mentor.entity.Report;
import intern.nhhtuan.toeic_mentor.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface INotificationService {
    void createReportNotifications(Report report);
  
    void createResponseUserNotifications(ReportDetailDTO reportDetailDTO);

    void createUserStreakAchievementRevokedNotification(String email, String milestoneTitle, int dayTarget);


    void createUserStreakAchievementNotifications(String email, String mileStoneTitle, int dayTarget);

    void createGoalCompletedNotifications(User user, String title, int actualValue, int targetValue, String unit);

    void createGoalFailedNotifications(User user, String title, int actualValue, int targetValue, String unit);

    int countUnreadNotifications(String email);

    List<NotificationResponse> getNotificationResponses(String email, LocalDateTime before, int pageSize);

    NotificationDetailResponse getNotificationDetailResponse(Long notificationId);

    boolean markIsRead(Long notificationId);

    boolean markAllAsRead(String email);
}
