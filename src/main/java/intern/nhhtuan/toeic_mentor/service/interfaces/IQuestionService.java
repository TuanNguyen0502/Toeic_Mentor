package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.QuestionDTO;
import intern.nhhtuan.toeic_mentor.dto.request.TestRequest;
import intern.nhhtuan.toeic_mentor.dto.response.QuestionResponse;
import intern.nhhtuan.toeic_mentor.dto.response.SectionQuestionResponse;
import intern.nhhtuan.toeic_mentor.entity.Question;
import intern.nhhtuan.toeic_mentor.entity.enums.EQuestionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IQuestionService {
    @Transactional
    void saveQuestionsFromDTO(List<QuestionDTO> dtoList);

    List<SectionQuestionResponse> getQuestionResponseBySectionId(Long sectionId);

    boolean updateQuestionStatus(Long questionId, EQuestionStatus status);

    List<QuestionResponse> generateTest(TestRequest request);

    int countByPart_Id(Long partId);

    int getTotalQuestions();

    Optional<Question> findById(Long aLong);
}
