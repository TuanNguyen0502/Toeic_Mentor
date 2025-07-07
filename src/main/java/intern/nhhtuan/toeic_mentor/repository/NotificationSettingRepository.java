package intern.nhhtuan.toeic_mentor.repository;

import intern.nhhtuan.toeic_mentor.entity.NotificationSetting;
import intern.nhhtuan.toeic_mentor.entity.enums.ENotificationTypeAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NotificationSettingRepository extends JpaRepository<NotificationSetting, Long> {
    @Query("""
    SELECT ns.enabled
    FROM NotificationSetting ns
    WHERE ns.user.id = :userId
      AND ns.notificationType.action = :action
    """)
    Optional<Boolean> isEnabled(@Param("userId") Long userId, @Param("action") ENotificationTypeAction action);

}
