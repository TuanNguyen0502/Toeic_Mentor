package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.request.AnswerRequest;
import intern.nhhtuan.toeic_mentor.dto.request.TestCountRequest;
import intern.nhhtuan.toeic_mentor.dto.response.TestCountResponse;
import intern.nhhtuan.toeic_mentor.dto.response.TestResultResponse;
import intern.nhhtuan.toeic_mentor.entity.Answer;
import intern.nhhtuan.toeic_mentor.entity.Part;
import intern.nhhtuan.toeic_mentor.entity.Test;
import intern.nhhtuan.toeic_mentor.entity.TestPart;
import intern.nhhtuan.toeic_mentor.entity.enums.EPart;
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
    public List<TestCountResponse> countByPartsAndPercent(TestCountRequest testCountRequest) {
        // Check if the request is for combine or separate parts
        if (testCountRequest.getType() == TestCountRequest.EType.COMBINE) {
            return countByCombinePartsAndPercent(testCountRequest);
        } else {
            return countBySeparatePartsAndPercent(testCountRequest);
        }
    }

    private List<TestCountResponse> countByCombinePartsAndPercent(TestCountRequest testCountRequest) {
        List<Long> partIds = partService.getIdsByPartName(testCountRequest.getParts()); // Get part ids by EPart names

        // Get tests that contains all parts in partIds
        List<Test> tests = testPartService.findTestsByCombinePartNames(partIds);
        if (tests.isEmpty()) {
            return List.of(TestCountResponse.builder()
                    .partName("No tests found for the specified parts")
                    .tests(0)
                    .build());
        }

        // Count correct answers for each test
        int testCount = 0;
        for (Test test : tests) {
            // Get total questions in the test that belong to the specified parts
            long totalQuestionsByPart = test.getAnswers() // Get all answers for the test
                    .stream()
                    // If the answer's question part is in partIds
                    .filter(answer -> partIds.contains(answer.getQuestion().getPart().getId()))
                    .count();

            if (totalQuestionsByPart == 0) continue; // Skip if no questions in the specified parts

            // Get total correct answers in the test that belong to the specified parts
            long totalAnswersByPart = test.getAnswers() // Get all answers for the test
                    .stream()
                    // If the answer's question part is in partIds and the answer is correct
                    .filter(answer -> partIds.contains(answer.getQuestion().getPart().getId()) && answerService.checkByStatus(answer.getId(), testCountRequest.getStatus()))
                    .count();
            // Check if the percentage of correct answers meets the requirement
            if (checkPercentCondition(totalAnswersByPart, totalQuestionsByPart, testCountRequest.getPercentChoice(), testCountRequest.getLowerRange(), testCountRequest.getUpperRange())) {
                testCount++;
            }
        }

        StringBuilder partName = new StringBuilder();
        for (EPart ePart : testCountRequest.getParts()) {
            partName.append(ePart.name().replace("_", " ")).append(", ");
        }
        partName = new StringBuilder(partName.substring(0, partName.length() - 2));
        return List.of(TestCountResponse.builder()
                .partName(partName.toString())
                .tests(testCount)
                .build());
    }

    private List<TestCountResponse> countBySeparatePartsAndPercent(TestCountRequest testCountRequest) {
        List<TestCountResponse> testCountResponses = new ArrayList<>();

        for (EPart ePart : testCountRequest.getParts()) {
            // Get part by EPart name
            Part part = partService.findByName(ePart);
            // Get tests that contains the part
            List<Test> tests = testPartService.findTestsByPartId(part.getId());
            // Count correct answers for each test
            int testCount = 0;
            for (Test test : tests) {
                // Get total questions in the test that belong to the specified parts
                long totalQuestionsByPart = test.getAnswers() // Get all answers for the test
                        .stream()
                        // If the answer's question part is equal to the specified part
                        .filter(answer -> answer.getQuestion().getPart().equals(part))
                        .count();

                if (totalQuestionsByPart == 0) continue; // Skip if no questions in the specified parts

                // Get total correct answers in the test that belong to the specified parts
                long totalAnswersByPart = test.getAnswers() // Get all answers for the test
                        .stream()
                        // If the answer's question part is equal to the specified part and the answer is correct
                        .filter(answer -> answer.getQuestion().getPart().equals(part) && answerService.checkByStatus(answer.getId(), testCountRequest.getStatus()))
                        .count();

                // Check if the percentage of correct answers meets the requirement
                if (checkPercentCondition(totalAnswersByPart, totalQuestionsByPart, testCountRequest.getPercentChoice(), testCountRequest.getLowerRange(), testCountRequest.getUpperRange())) {
                    testCount++;
                }
            }

            // Create TestCountResponse for each part
            testCountResponses.add(TestCountResponse.builder()
                    .partId(part.getId())
                    .partName(part.getName().name().replace("_", " "))
                    .tests(testCount)
                    .build());
        }

        return testCountResponses;
    }

    private boolean checkPercentCondition(long totalAnswers, long totalQuestions, TestCountRequest.EPercentChoice percentChoice, int lowerRange, int upperRange) {
        if (totalQuestions == 0) return false; // Avoid division by zero
        int percentage = (int) ((totalAnswers * 100) / totalQuestions);
        return switch (percentChoice) {
            case GREATER_THAN -> percentage > lowerRange;
            case GREATER_THAN_OR_EQUAL -> percentage >= lowerRange;
            case LESS_THAN -> percentage < lowerRange;
            case LESS_THAN_OR_EQUAL -> percentage <= lowerRange;
            case EQUAL_TO -> percentage == lowerRange;
            case BETWEEN -> percentage >= lowerRange && percentage <= upperRange;
            default -> false;
        };
    }

    @Override
    public int getTotalTests() {
        return testRepository.findAll().size();
    }

    @Transactional
    @Override
    public void saveTest(String email, TestResultResponse testResultResponse) {
        Test test = new Test();
        test.setScore(testResultResponse.getScore());
        test.setRecommendations(testResultResponse.getRecommendations());
        test.setUser(userService.findByEmail(email));
        test.setCreatedAt(LocalDateTime.now());

        // Lưu trước để có ID cho liên kết
        testRepository.save(test);

        // Tạo danh sách Answer từ AnswerRequest
        List<Answer> answers = new ArrayList<>();
        List<Part> parts = new ArrayList<>();
        for (TestResultResponse.AnswerResponse answerResponse : testResultResponse.getAnswerResponses()) {
            Answer answer = new Answer();
            answer.setAnswer(answerResponse.getUserAnswer());
            answer.setQuestion(questionService.findById(answerResponse.getId()).orElse(null));
            answer.setTest(test);
            answers.add(answer); // Lưu Answer vào danh sách
            // Lưu các Answer
            answerService.save(answer);

            // Lưu Part nếu chưa có
            Part part = partService.findByName(answerResponse.getPart());
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
