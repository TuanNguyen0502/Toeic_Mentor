package intern.nhhtuan.toeic_mentor.repository;

import intern.nhhtuan.toeic_mentor.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByReceiver_Email(String receiverEmail);

    List<Notification> findAllByReceiver_EmailAndCreatedAtBeforeOrderByCreatedAtDesc(
            String receiverEmail,
            LocalDateTime createdAtBefore,
            Pageable pageable
    );
}
