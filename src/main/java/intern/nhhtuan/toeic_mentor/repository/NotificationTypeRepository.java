package intern.nhhtuan.toeic_mentor.repository;

import intern.nhhtuan.toeic_mentor.entity.NotificationType;
import intern.nhhtuan.toeic_mentor.entity.enums.ENotificationTypeAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTypeRepository extends JpaRepository<NotificationType, Long> {
    NotificationType findByAction(ENotificationTypeAction action);
}
