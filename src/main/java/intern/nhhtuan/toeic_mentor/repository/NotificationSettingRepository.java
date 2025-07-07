package intern.nhhtuan.toeic_mentor.repository;

import intern.nhhtuan.toeic_mentor.entity.NotificationSetting;
import intern.nhhtuan.toeic_mentor.entity.enums.ENotificationTypeAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationSettingRepository extends JpaRepository<NotificationSetting, Long> {
    NotificationSetting findByUser_EmailAndNotificationType_Action(String userEmail, ENotificationTypeAction notificationTypeAction);
}
