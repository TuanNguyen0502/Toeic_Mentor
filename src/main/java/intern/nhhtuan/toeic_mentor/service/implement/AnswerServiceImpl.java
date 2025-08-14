package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.request.TestCountRequest;
import intern.nhhtuan.toeic_mentor.entity.Answer;
import intern.nhhtuan.toeic_mentor.repository.AnswerRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.IAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements IAnswerService {
    private final AnswerRepository answerRepository;

    @Override
    public boolean checkByStatus(Long answerId, TestCountRequest.EStatus status) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("Answer not found with id: " + answerId));
        if (status.equals(TestCountRequest.EStatus.CORRECT)) {
            // Check if the answer is correct
            // If answer is correct then return true, else return false
            return answer.getAnswer().equals(answer.getQuestion().getCorrectAnswer());
        } else {
            // Check if the answer is incorrect
            // If answer is incorrect then return true, else return false
            return !answer.getAnswer().equals(answer.getQuestion().getCorrectAnswer());
        }
    }

    @Override
    public void save(Answer answer) {
        answerRepository.save(answer);
    }

    @Override
    @Transactional
    public void saveAll(List<Answer> answers) {
        answerRepository.saveAll(answers);
    }

    @Transactional
    @Override
    public void deleteAllByTestId(Long testId) {
        answerRepository.deleteAllByTest_Id(testId);
    }
}
