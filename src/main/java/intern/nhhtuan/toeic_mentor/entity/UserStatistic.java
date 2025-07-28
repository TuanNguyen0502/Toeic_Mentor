package intern.nhhtuan.toeic_mentor.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_statistics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "estimated_score")
    private Integer estimatedScore; // Estimated score based on user's performance

    @Column(name = "score_interval")
    private Integer scoreInterval; // Interval for estimated score, e.g., 5 points

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // User associated with the statistic

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
