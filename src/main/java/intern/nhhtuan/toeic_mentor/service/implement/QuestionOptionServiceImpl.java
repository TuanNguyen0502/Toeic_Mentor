package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.entity.Question;
import intern.nhhtuan.toeic_mentor.entity.QuestionOption;
import intern.nhhtuan.toeic_mentor.repository.QuestionOptionRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.IQuestionOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuestionOptionServiceImpl implements IQuestionOptionService {
    private final QuestionOptionRepository questionOptionRepository;

    @Override
    public boolean saveOptions(Map<String, String> options, Question question) {
        if (options == null || options.isEmpty()) {
            return false; // No options to save
        }

        // Clear existing options for the question
        List<QuestionOption> questionOptions = questionOptionRepository.findByQuestion_Id(question.getId());
        if (questionOptions != null && !questionOptions.isEmpty()) {
            questionOptionRepository.deleteAll(questionOptions);
        }

        // Save new options
        for (var entry : options.entrySet()) {
            QuestionOption option = new QuestionOption(entry.getKey(), entry.getValue(), question);
            questionOptionRepository.save(option);
        }
        return true; // Options saved successfully
    }
}
