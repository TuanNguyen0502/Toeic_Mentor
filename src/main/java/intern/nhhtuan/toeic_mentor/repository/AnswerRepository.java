package intern.nhhtuan.toeic_mentor.repository;

import intern.nhhtuan.toeic_mentor.dto.QuestionAnswerStats;
import intern.nhhtuan.toeic_mentor.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query("SELECT " +
            "a.question.id as questionId, " +
            "SUM(CASE WHEN a.isCorrect = true THEN 1 ELSE 0 END) as correctCount, " +
            "SUM(CASE WHEN a.isCorrect = false THEN 1 ELSE 0 END) as wrongCount, " +
            "COUNT(a) as totalCount " +
            "FROM Answer a " +
            "GROUP BY a.question.id")
    List<QuestionAnswerStats> getAnswerStatistics();
}
