package intern.nhhtuan.toeic_mentor.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

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

    @ManyToMany(mappedBy = "notificationTypes")
    private Set<Role> roles;
}
