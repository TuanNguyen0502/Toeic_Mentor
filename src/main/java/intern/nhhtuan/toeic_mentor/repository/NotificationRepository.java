package intern.nhhtuan.toeic_mentor.repository;

import intern.nhhtuan.toeic_mentor.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
