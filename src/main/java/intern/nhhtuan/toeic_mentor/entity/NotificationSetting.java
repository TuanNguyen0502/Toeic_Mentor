package intern.nhhtuan.toeic_mentor.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationSetting {

    @EmbeddedId
    private NotificationSettingId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("notificationType")
    @JoinColumn(name = "notification_type")
    private NotificationType notificationType;

    @Column(nullable = false)
    private boolean enabled = true;
}
