package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.entity.NotificationType;
import intern.nhhtuan.toeic_mentor.entity.Role;
import intern.nhhtuan.toeic_mentor.entity.RoleNotification;
import intern.nhhtuan.toeic_mentor.repository.RoleNotificationRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.IRoleNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleNotificationServiceImpl implements IRoleNotificationService {
    private final RoleNotificationRepository roleNotificationRepository;

    @Override
    public List<NotificationType> getNotificationTypesByRole(Role role) {
        // Fetch the notification types associated with the user's role
        return roleNotificationRepository.findAllByRole(role).stream()
                .map(RoleNotification::getNotificationType)
                .toList();
    }
}
