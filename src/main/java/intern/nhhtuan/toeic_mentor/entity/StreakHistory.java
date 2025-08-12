package intern.nhhtuan.toeic_mentor.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "streak_histories")
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StreakHistory extends TrackingDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_streak", nullable = false)
    private LocalDate startStreak;

    @Column(name = "end_streak")
    private LocalDate endStreak;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}