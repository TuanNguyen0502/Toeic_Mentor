package intern.nhhtuan.toeic_mentor.entity;

import intern.nhhtuan.toeic_mentor.entity.enums.EReportStatus;
import intern.nhhtuan.toeic_mentor.entity.enums.EReportType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reports")
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report extends TrackingDate {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private String id;

    @Enumerated(EnumType.STRING)
    private EReportType category;

    private String description; // e.g., "Inappropriate content", "Spam", "Off-topic"

    @Enumerated(EnumType.STRING)
    private EReportStatus status; // e.g., "pending", "resolved", "rejected"

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
}
