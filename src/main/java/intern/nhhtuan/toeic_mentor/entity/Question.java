package intern.nhhtuan.toeic_mentor.entity;

import intern.nhhtuan.toeic_mentor.entity.enums.EQuestionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_number", nullable = false)
    private Integer questionNumber;

    @Column(name = "question_text", columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "correct_answer", length = 1)
    private String correctAnswer;

    @Column(name = "passage", columnDefinition = "TEXT")
    private String passage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EQuestionStatus status;

    @Column(name = "answer_explanation", columnDefinition = "TEXT")
    private String answerExplanation;

    @ManyToOne
    @JoinColumn(name = "part_id", referencedColumnName = "id")
    private Part part;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionImage> passageImageUrls = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionTag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionOption> options = new ArrayList<>();
}