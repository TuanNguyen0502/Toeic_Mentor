package intern.nhhtuan.toeic_mentor.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "role_notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleNotification {

    @EmbeddedId
    private RoleNotificationId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("notificationTypeId")
    @JoinColumn(name = "notification_type_id")
    private NotificationType notificationType;
}
