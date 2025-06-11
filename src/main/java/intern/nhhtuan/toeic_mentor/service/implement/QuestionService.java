package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.QuestionDTO;
import intern.nhhtuan.toeic_mentor.entity.Question;
import intern.nhhtuan.toeic_mentor.entity.QuestionOption;
import intern.nhhtuan.toeic_mentor.entity.QuestionTag;
import intern.nhhtuan.toeic_mentor.repository.QuestionOptionRepository;
import intern.nhhtuan.toeic_mentor.repository.QuestionRepository;
import intern.nhhtuan.toeic_mentor.repository.QuestionTagRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.IQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService implements IQuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository optionRepository;
    private final QuestionTagRepository tagRepository;

    @Transactional
    @Override
    public void saveQuestionsFromDTO(List<QuestionDTO> dtoList) {
        for (QuestionDTO dto : dtoList) {
            Question question = new Question();
            question.setQuestionNumber(dto.getQuestionNumber());
            question.setQuestionText(dto.getQuestionText());
            question.setCorrectAnswer(dto.getCorrectAnswer());
            question.setPassage(dto.getPassage());
            question.setPassageImageUrl(dto.getPassageImageUrl());
            question.setPart(dto.getPart());

            // Lưu trước để có ID cho liên kết
            questionRepository.save(question);

            // Lưu options
            for (var entry : dto.getOptions().entrySet()) {
                QuestionOption option = new QuestionOption(
                        entry.getKey(),
                        entry.getValue(),
                        question
                );
                optionRepository.save(option);
            }

            // Lưu tags
            for (String tagStr : dto.getTags()) {
                QuestionTag tag = new QuestionTag(tagStr, question);
                tagRepository.save(tag);
            }
        }
    }
}
