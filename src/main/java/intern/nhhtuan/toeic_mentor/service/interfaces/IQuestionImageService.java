package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.QuestionImageUpdateDTO;

public interface IQuestionImageService {
    QuestionImageUpdateDTO getQuestionImageUpdateByQuestionId(Long questionId);

    boolean updateQuestionImage(QuestionImageUpdateDTO questionImageUpdateDTO);
}
