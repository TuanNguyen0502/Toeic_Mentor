package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.request.AnswerRequest;
import intern.nhhtuan.toeic_mentor.entity.Answer;
import intern.nhhtuan.toeic_mentor.entity.Part;
import intern.nhhtuan.toeic_mentor.entity.Test;
import intern.nhhtuan.toeic_mentor.entity.TestPart;
import intern.nhhtuan.toeic_mentor.repository.TestRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements ITestService {
    private final TestRepository testRepository;
    private final ITestPartService testPartService;
    private final IAnswerService answerService;
    private final IUserService userService;
    private final IQuestionService questionService;
    private final IPartService partService;

    @Override
    public int countCorrectByCombinePartsAndPercent(List<Long> partIds, int percent) {
        // Get tests that contains all parts in partIds
        List<Test> tests = testPartService.findTestsByCombinePartIds(partIds);
        if (tests.isEmpty()) {
            return 0;
        }

        // Count correct answers for each test
        int correctCount = 0;
        for (Test test : tests) {
            // Get total questions in the test that belong to the specified parts
            long totalQuestionsByPart = test.getAnswers() // Get all answers for the test
                    .stream()
                    // If the answer's question part is in partIds
                    .filter(answer -> partIds.contains(answer.getQuestion().getPart().getId()))
                    .count();

            if (totalQuestionsByPart == 0) continue; // Skip if no questions in the specified parts

            long totalCorrectByPart = test.getAnswers() // Get all answers for the test
                    .stream()
                    // If the answer's question part is in partIds and the answer is correct
                    .filter(answer -> partIds.contains(answer.getQuestion().getPart().getId()) && answerService.isCorrect(answer.getId()))
                    .count();
            // Check if the percentage of correct answers meets the requirement
            if ((totalCorrectByPart * 100 / totalQuestionsByPart) >= percent) {
                correctCount++;
            }
        }

        return correctCount;
    }

    @Override
    public int getTotalTests() {
        return testRepository.findAll().size();
    }

    @Transactional
    @Override
    public void saveTests(String email, List<AnswerRequest> answerRequests) {
        Test test = new Test();
        test.setUser(userService.findByEmail(email));
        test.setCreatedAt(LocalDateTime.now());

        // Lưu trước để có ID cho liên kết
        testRepository.save(test);

        // Tạo danh sách Answer từ AnswerRequest
        List<Answer> answers = new ArrayList<>();
        List<Part> parts = new ArrayList<>();
        for (AnswerRequest answerRequest : answerRequests) {
            Answer answer = new Answer();
            answer.setAnswer(answerRequest.getUserAnswer());
            answer.setQuestion(questionService.findById(answerRequest.getId()).orElse(null));
            answer.setTest(test);
            answers.add(answer); // Lưu Answer vào danh sách
            // Lưu các Answer
            answerService.save(answer);

            // Lưu Part nếu chưa có
            Part part = partService.findByName(answerRequest.getPart());
            if (part != null && !parts.contains(part)) {
                parts.add(part);
            }
        }

        test.setAnswers(answers);
        testRepository.save(test); // Cập nhật Test với danh sách Answer

        // Lưu các Part liên kết với Test
        for (Part part : parts) {
            TestPart testPart = new TestPart();
            testPart.setTest(test);
            testPart.setPart(part);
            testPartService.save(testPart);
        }
    }
}
