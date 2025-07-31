package intern.nhhtuan.toeic_mentor.entity;

import intern.nhhtuan.toeic_mentor.entity.enums.EGoalStatus;
import intern.nhhtuan.toeic_mentor.entity.enums.EGoalType;
import intern.nhhtuan.toeic_mentor.entity.enums.EGoalUnit;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "goals")
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Goal extends TrackingDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private EGoalType type;

    @Column(name = "goal_date", nullable = false)
    private LocalDateTime goalDate;

    @Column(name = "target_value", nullable = false)
    private Integer targetValue;

    @Column(name = "actual_value", nullable = false)
    private Integer actualValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit", nullable = false)
    private EGoalUnit unit;

    @Column(name = "part")
    private Integer part;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EGoalStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
