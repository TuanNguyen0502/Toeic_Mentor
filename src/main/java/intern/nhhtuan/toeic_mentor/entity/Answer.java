package intern.nhhtuan.toeic_mentor.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "answers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1, nullable = false)
    private String answer;

    @Column(name = "is_correct", nullable = false)
    private boolean isCorrect; // Indicates if the answer is correct

    private String explanation; // Explanation for the answer, if any

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;
}
