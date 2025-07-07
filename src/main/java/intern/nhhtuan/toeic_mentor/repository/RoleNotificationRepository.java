package intern.nhhtuan.toeic_mentor.repository;

import intern.nhhtuan.toeic_mentor.entity.Role;
import intern.nhhtuan.toeic_mentor.entity.RoleNotification;
import intern.nhhtuan.toeic_mentor.entity.RoleNotificationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleNotificationRepository extends JpaRepository<RoleNotification, RoleNotificationId> {
    List<RoleNotification> findAllByRole(Role role);
}
