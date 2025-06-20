package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.entity.Answer;
import intern.nhhtuan.toeic_mentor.repository.AnswerRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.IAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements IAnswerService {
    private final AnswerRepository answerRepository;

    @Override
    public boolean isCorrect(Long answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("Answer not found with id: " + answerId));
        return answer.getAnswer().equals(answer.getQuestion().getCorrectAnswer());
    }

    @Override
    public void save(Answer answer) {
        answerRepository.save(answer);
    }
}
