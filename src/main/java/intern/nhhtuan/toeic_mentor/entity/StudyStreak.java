package intern.nhhtuan.toeic_mentor.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "study_streaks")
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyStreak extends TrackingDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "current_streak", nullable = false)
    private Integer currentStreak;

    @Column(name = "max_streak", nullable = false)
    private Integer maxStreak;

    @Column(name = "last_study_date", nullable = false)
    private LocalDate lastStudyDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
