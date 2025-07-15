package intern.nhhtuan.toeic_mentor.entity;

import intern.nhhtuan.toeic_mentor.util.StringListJsonConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @Column(name = "time_spent", nullable = false)
    private Integer timeSpent; // Time spent on the question in seconds

    @Convert(converter = StringListJsonConverter.class)
    @Column(columnDefinition = "json")
    private List<String> answerExplanation;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;
}
