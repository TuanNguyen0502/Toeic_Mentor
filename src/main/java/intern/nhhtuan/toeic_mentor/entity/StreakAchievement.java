package intern.nhhtuan.toeic_mentor.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "streak_achievements",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "milestone"})}
)
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StreakAchievement extends TrackingDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "milestone_id", nullable = false)
    private StreakMilestone streakMilestone;

    @Column(name = "achieved_at", nullable = false)
    private LocalDateTime achievedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
