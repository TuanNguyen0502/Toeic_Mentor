package intern.nhhtuan.toeic_mentor.repository;

import intern.nhhtuan.toeic_mentor.entity.Question;
import intern.nhhtuan.toeic_mentor.entity.QuestionTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("SELECT DISTINCT q FROM Question q WHERE q.part = :part")
    List<Question> findDistinctByPart(@Param("part") Integer part);

    @Query("SELECT DISTINCT q FROM Question q JOIN q.tags t WHERE q.part = :part AND t IN :tags")
    List<Question> findDistinctByPartAndTags(@Param("part") Integer part, @Param("tags") List<QuestionTag> tags);
}
