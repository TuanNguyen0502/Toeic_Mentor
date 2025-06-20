package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.request.AnswerRequest;
import intern.nhhtuan.toeic_mentor.dto.request.QuestionRequest;
import intern.nhhtuan.toeic_mentor.dto.request.TestRequest;
import intern.nhhtuan.toeic_mentor.dto.response.QuestionResponse;
import intern.nhhtuan.toeic_mentor.entity.Question;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IQuestionService {
    @Transactional
    void saveQuestionsFromDTO(List<QuestionRequest> dtoList);

    List<QuestionResponse> generateTest(TestRequest request);

    int countByPart_Id(Long partId);

    int getTotalQuestions();

    Optional<Question> findById(Long aLong);
}
