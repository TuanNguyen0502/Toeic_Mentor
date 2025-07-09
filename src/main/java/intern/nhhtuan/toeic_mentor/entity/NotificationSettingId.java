package intern.nhhtuan.toeic_mentor.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSettingId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "notification_type_id")
    private Long notificationTypeId;
}
