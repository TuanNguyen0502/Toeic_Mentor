package intern.nhhtuan.toeic_mentor.repository;

import intern.nhhtuan.toeic_mentor.entity.Question;
import intern.nhhtuan.toeic_mentor.entity.QuestionTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByPart(Integer part);

    List<Question> findByPartAndTags(Integer part, List<QuestionTag> tags);
}
