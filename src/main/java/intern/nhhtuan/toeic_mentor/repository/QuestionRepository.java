package intern.nhhtuan.toeic_mentor.repository;

import intern.nhhtuan.toeic_mentor.entity.Question;
import intern.nhhtuan.toeic_mentor.entity.QuestionTag;
import intern.nhhtuan.toeic_mentor.entity.enums.EPart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findDistinctByPart_Name(EPart partName);

    List<Question> findDistinctByPart_NameAndTags(EPart partName, List<QuestionTag> tags);

    int countByPart_Id(Long partId);
}
