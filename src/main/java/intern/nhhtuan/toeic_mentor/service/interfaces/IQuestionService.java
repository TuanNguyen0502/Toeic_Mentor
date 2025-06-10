package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.QuestionDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IQuestionService {
    @Transactional
    void saveQuestionsFromDTO(List<QuestionDTO> dtoList);
}
