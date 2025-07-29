package intern.nhhtuan.toeic_mentor.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "streak_milestones")
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StreakMilestone extends TrackingDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "day_target", nullable = false, unique = true)
    private Integer dayTarget;

    @Column(name = "title", columnDefinition = "VARCHAR(255)", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
