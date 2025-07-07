package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.entity.NotificationType;
import intern.nhhtuan.toeic_mentor.entity.Role;

import java.util.List;

public interface IRoleNotificationService {
    List<NotificationType> getNotificationTypesByRole(Role role);
}
