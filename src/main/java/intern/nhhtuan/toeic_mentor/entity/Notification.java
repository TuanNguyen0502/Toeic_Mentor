package intern.nhhtuan.toeic_mentor.entity;

import intern.nhhtuan.toeic_mentor.entity.enums.ENotificationType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reports")
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends TrackingDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ENotificationType type;

    @Column(nullable = false)
    private String title;

    private String message;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "report_id", nullable = true)
    private Report report;
}
