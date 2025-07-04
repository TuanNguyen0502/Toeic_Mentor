package intern.nhhtuan.toeic_mentor.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification_types")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationType {
    @Id
    @Column(name = "action", length = 100)
    private String action;

    @Column(name = "description")
    private String description;
}
