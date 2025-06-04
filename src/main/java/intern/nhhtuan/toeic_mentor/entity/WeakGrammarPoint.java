package intern.nhhtuan.toeic_mentor.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "weak_grammar_points")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeakGrammarPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String grammarPoint;

    private int mistakeCount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
