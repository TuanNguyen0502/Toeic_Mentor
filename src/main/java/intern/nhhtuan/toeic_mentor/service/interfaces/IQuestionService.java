package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.QuestionDTO;
import intern.nhhtuan.toeic_mentor.dto.request.TestRequest;
import intern.nhhtuan.toeic_mentor.dto.response.QuestionResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IQuestionService {
    @Transactional
    void saveQuestionsFromDTO(List<QuestionDTO> dtoList);

    List<QuestionResponse> generateTest(TestRequest request);
}
