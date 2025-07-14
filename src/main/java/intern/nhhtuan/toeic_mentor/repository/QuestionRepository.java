package intern.nhhtuan.toeic_mentor.repository;

import intern.nhhtuan.toeic_mentor.entity.Question;
import intern.nhhtuan.toeic_mentor.entity.enums.EPart;
import intern.nhhtuan.toeic_mentor.entity.enums.EQuestionStatus;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findDistinctByPart_NameAndStatus(EPart partName, EQuestionStatus status);

    List<Question> findDistinctByPart_NameAndStatus(EPart partName, EQuestionStatus status, Limit limit);

    List<Question> findDistinctByPart_NameAndTagsContainingAndStatus(EPart part_name, List<String> tags, EQuestionStatus status);

    int countByPart_Id(Long partId);

    List<Question> findBySection_Id(Long sectionId);
}
