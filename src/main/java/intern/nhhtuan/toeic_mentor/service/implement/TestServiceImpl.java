package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.request.GoalProgressUpdateRequest;
import intern.nhhtuan.toeic_mentor.dto.request.TestCountRequest;
import intern.nhhtuan.toeic_mentor.dto.request.UncompletedAnswerRequest;
import intern.nhhtuan.toeic_mentor.dto.response.*;
import intern.nhhtuan.toeic_mentor.entity.*;
import intern.nhhtuan.toeic_mentor.entity.enums.EPart;
import intern.nhhtuan.toeic_mentor.exception.ResourceNotFoundException;
import intern.nhhtuan.toeic_mentor.exception.UnauthorizedException;
import intern.nhhtuan.toeic_mentor.repository.TestRepository;
import intern.nhhtuan.toeic_mentor.repository.UserRepository;
import intern.nhhtuan.toeic_mentor.repository.specification.TestSpecification;
import intern.nhhtuan.toeic_mentor.service.interfaces.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements ITestService {
    private final TestRepository testRepository;
    private final ITestPartService testPartService;
    private final IAnswerService answerService;
    private final IUserService userService;
    private final IQuestionService questionService;
    private final IPartService partService;
    private final IStudyStreakService studyStreakService;
    private final UserRepository userRepository;
    private final IGoalService goalService;

    @Override
    public Page<TestHistoryResponse> getTestHistoryResponses(String email,
                                                             LocalDateTime createdAtStart,
                                                             LocalDateTime createdAtEnd,
                                                             Boolean completed,
                                                             int page,
                                                             int size,
                                                             String sortBy,
                                                             String direction) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found with email: " + email));

        Specification<Test> specification = (root, query, cb) -> cb.conjunction(); // Bắt đầu với 1 điều kiện TRUE
        if (user != null) {
            specification = specification.and(TestSpecification.belongsToUser(user.getId()));
        }
        if (createdAtStart != null || createdAtEnd != null) {
            specification = specification.and(TestSpecification.createdAtBetween(createdAtStart, createdAtEnd));
        }
        if (completed != null) {
            specification = specification.and(TestSpecification.completed(completed));
        }

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return testRepository.findAll(specification, pageable)
                .map(test -> {
                    int totalQuestions = test.getAnswers().size();
                    int timeSpent = test.getAnswers().stream()
                            .mapToInt(answer -> Objects.requireNonNullElse(answer.getTimeSpent(), 0))
                            .sum();
                    String parts = test.getAnswers().stream()
                            .map(answer -> answer.getQuestion().getPart().getName().name().replace("_", " "))
                            .distinct()
                            .collect(Collectors.joining("\n"));
                    return TestHistoryResponse.builder()
                            .id(test.getId())
                            .correctAnswers(test.getScore())
                            .totalQuestions(totalQuestions)
                            .parts(parts.isEmpty() ? "" : parts)
                            .timeSpent(timeSpent)
                            .completedAt(test.getCompletedAt() != null
                                    ? test.getCompletedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                    : "Not completed")
                            .createdAt(test.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                            .build();
                });
    }

    @Override
    public TestHistoryDetailResponse getTestHistoryDetailResponseById(Long testId) {
        Optional<Test> testOpt = testRepository.findById(testId);
        if (testOpt.isEmpty()) {
            throw new ResourceNotFoundException("Test", "id", testId);
        }

        Test test = testOpt.get();

        // Convert Test entity to TestResultResponse
        List<TestHistoryDetailResponse.AnswerResponse> answerResponses = new ArrayList<>();

        for (Answer answer : test.getAnswers()) {
            Question question = answer.getQuestion();

            // Convert options to OptionResponse format
            List<TestHistoryDetailResponse.OptionResponse> options = question.getOptions().stream()
                    .map(opt -> TestHistoryDetailResponse.OptionResponse.builder()
                            .key(opt.getKey())
                            .value(opt.getValue())
                            .build())
                    .collect(Collectors.toList());
            List<String> questionImages = question.getPassageImageUrls().stream()
                    .map(QuestionImage::getImage)
                    .toList();

            TestHistoryDetailResponse.AnswerResponse answerResponse = TestHistoryDetailResponse.AnswerResponse.builder()
                    .answerId(answer.getId())
                    .userAnswer(answer.getAnswer()) // Get user's answer from answer
                    .isCorrect(answer.isCorrect())
                    .questionText(question.getQuestionText())
                    .optionExplanation(answer.getAnswerExplanation())
                    .correctAnswer(question.getCorrectAnswer()) // Get correct answer from question
                    .timeSpent(answer.getTimeSpent())
                    .questionId(question.getId())
                    .correctAnswer(question.getCorrectAnswer())
                    .passage(question.getPassage())
                    .questionText(question.getQuestionText())
                    .part(Integer.parseInt(question.getPart().getName().toString().replace("PART_", "")))
                    .difficulty(question.getDifficulty())
                    .tags(question.getTags())
                    .questionImages(questionImages)
                    .options(options)
                    .build();

            answerResponses.add(answerResponse);
        }

        return TestHistoryDetailResponse.builder()
                .testId(test.getId())
                .score(test.getScore())
                .correctPercent((int) ((test.getScore() * 100.0) / answerResponses.size()))
                .recommendations(test.getRecommendations())
                .performance(test.getPerformance())
                .referenceUrls(test.getReferenceUrls())
                .createdAt(test.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .answerResponses(answerResponses)
                .build();
    }

    @Override
    public int getTotalTests() {
        return testRepository.findAll().size();
    }

    @Override
    public TestResultResponse getTestResult(Long testId, String email) {
        Optional<Test> testOpt = testRepository.findById(testId);
        if (testOpt.isEmpty()) {
            throw new RuntimeException("Test not found with ID: " + testId);
        }

        Test test = testOpt.get();

        // Verify that the test belongs to the user
        if (!test.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Access denied: Test does not belong to user: " + email);
        }

        // Convert Test entity to TestResultResponse
        List<TestResultResponse.AnswerResponse> answerResponses = new ArrayList<>();

        for (Answer answer : test.getAnswers()) {
            Question question = answer.getQuestion();

            // Convert options to OptionResponse format
            List<TestResultResponse.OptionResponse> options = question.getOptions().stream()
                    .map(opt -> TestResultResponse.OptionResponse.builder()
                            .key(opt.getKey())
                            .value(opt.getValue())
                            .build())
                    .collect(Collectors.toList());

            TestResultResponse.AnswerResponse answerResponse = TestResultResponse.AnswerResponse.builder()
                    .id(question.getId())
                    .questionText(question.getQuestionText())
                    .correctAnswer(question.getCorrectAnswer()) // Get correct answer from question
                    .userAnswer(answer.getAnswer()) // Get user's answer from answer
                    .part(Integer.valueOf(question.getPart().getName().toString().replace("PART_", "")))
                    .options(options)
                    .tags(question.getTags())
                    .timeSpent(answer.getTimeSpent())
                    .isCorrect(answer.isCorrect())
                    .optionExplanation(answer.getAnswerExplanation())
                    .build();

            answerResponses.add(answerResponse);
        }

        return TestResultResponse.builder()
                .testId(test.getId())
                .score(test.getScore())
                .correctPercent((int) ((test.getScore() * 100.0) / answerResponses.size()))
                .answerResponses(answerResponses)
                .recommendations(test.getRecommendations())
                .performance(test.getPerformance())
                .referenceUrls(test.getReferenceUrls())
                .build();
    }

    @Override
    public List<RecentTestResponse> getRecentTests(String email, int number) {
        Pageable pageable = PageRequest.of(0, number);

        // todo check email is valid
        List<Test> tests = testRepository.findRecentTestsByUserEmail(email, pageable);

        return tests.stream().map(this::mapToRecentTestResponse).collect(Collectors.toList());
    }

    @Override
    public List<QuestionResponse> getUncompletedTestQuestions(Long testId) {
        // Find the test by ID and user
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new ResourceNotFoundException("Test", "id", testId));

        // Convert Test entity to QuestionResponse
        List<QuestionResponse> questionResponses = new ArrayList<>();
        for (Answer answer : test.getAnswers()) {
            Question question = answer.getQuestion();
            List<String> questionImages = question.getPassageImageUrls().stream()
                    .map(QuestionImage::getImage)
                    .toList();

            QuestionResponse questionResponse = QuestionResponse.builder()
                    .id(question.getId())
                    .questionText(question.getQuestionText())
                    .correctAnswer(question.getCorrectAnswer())
                    .answerExplanation(question.getAnswerExplanation())
                    .userAnswer(answer.getAnswer())
                    .passage(question.getPassage())
                    .passageImageUrls(questionImages)
                    .part(Integer.parseInt(question.getPart().getName().toString().replace("PART_", "")))
                    .options(question.getOptions().stream()
                            .map(opt -> QuestionResponse.OptionResponse.builder()
                                    .key(opt.getKey())
                                    .value(opt.getValue())
                                    .build())
                            .collect(Collectors.toList()))
                    .tags(question.getTags())
                    .difficulty(question.getDifficulty())
                    .timeSpent(answer.getTimeSpent())
                    .build();

            questionResponses.add(questionResponse);
        }

        return questionResponses;
    }

    @Override
    public List<TestCountResponse> countByPartsAndPercent(TestCountRequest testCountRequest) {
        // Check if the request is for combine or separate parts
        if (testCountRequest.getType() == TestCountRequest.EType.COMBINE) {
            return countByCombinePartsAndPercent(testCountRequest);
        } else {
            return countBySeparatePartsAndPercent(testCountRequest);
        }
    }

    @Transactional
    @Override
    public void saveTest(String email, TestResultResponse testResultResponse) {
        Test test = new Test();
        test.setScore(testResultResponse.getScore());
        test.setRecommendations(testResultResponse.getRecommendations());
        test.setPerformance(testResultResponse.getPerformance());
        test.setReferenceUrls(testResultResponse.getReferenceUrls());
        test.setUser(userService.findByEmail(email));
        test.setCreatedAt(LocalDateTime.now());
        test.setCompletedAt(LocalDateTime.now()); // Set completed time for the test

        // Lưu trước để có ID cho liên kết
        testRepository.save(test);

        // Tạo danh sách Answer từ AnswerRequest
        List<Answer> answers = new ArrayList<>();
        List<Part> parts = new ArrayList<>();
        for (TestResultResponse.AnswerResponse answerResponse : testResultResponse.getAnswerResponses()) {
            Answer answer = new Answer();
            answer.setAnswer(answerResponse.getUserAnswer());
            answer.setCorrect(answerResponse.isCorrect());
            answer.setAnswerExplanation(answerResponse.getOptionExplanation());
            answer.setTimeSpent(answerResponse.getTimeSpent());
            answer.setQuestion(questionService.findById(answerResponse.getId()).orElse(null));
            answer.setTest(test);
            answers.add(answer); // Lưu Answer vào danh sách
            // Lưu các Answer
            answerService.save(answer);
            answerResponse.setAnswerId(answer.getId());

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

        // Set the testId in the response
        testResultResponse.setTestId(test.getId());

        // Update study streak for the user
        studyStreakService.updateCurrentStreak(email);

        // Update goals for the user
        List<GoalProgressUpdateRequest> goalProgressUpdateRequests = testResultResponse.getAnswerResponses()
                .stream()
                .map(answerResponse -> GoalProgressUpdateRequest.builder()
                        .part(answerResponse.getPart())
                        .timeSpent(answerResponse.getTimeSpent())
                        .build())
                .toList();
        goalService.updateGoalProgressAfterTest(email, goalProgressUpdateRequests);
    }

    @Override
    public void saveTestById(Long testId, TestResultResponse testResultResponse) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new ResourceNotFoundException("Test", "id", testId));
        test.setScore(testResultResponse.getScore());
        test.setRecommendations(testResultResponse.getRecommendations());
        test.setPerformance(testResultResponse.getPerformance());
        test.setReferenceUrls(testResultResponse.getReferenceUrls());
        test.setCompletedAt(LocalDateTime.now()); // Set completed time for the test

        // Lưu trước để có ID cho liên kết
        testRepository.save(test);

        // Xóa các Answer cũ nếu có
        answerService.deleteAllByTestId(testId);

        // Tạo danh sách Answer từ AnswerRequest
        for (TestResultResponse.AnswerResponse answerResponse : testResultResponse.getAnswerResponses()) {
            Answer answer = new Answer();
            answer.setAnswer(answerResponse.getUserAnswer());
            answer.setCorrect(answerResponse.isCorrect());
            answer.setAnswerExplanation(answerResponse.getOptionExplanation());
            answer.setTimeSpent(answerResponse.getTimeSpent());
            answer.setQuestion(questionService.findById(answerResponse.getId()).orElse(null));
            answer.setTest(test);
            // Lưu các Answer
            answerService.save(answer);
            answerResponse.setAnswerId(answer.getId());
        }

        // Set the testId in the response
        testResultResponse.setTestId(test.getId());

        // Update study streak for the user
        studyStreakService.updateCurrentStreak(test.getUser().getEmail());

        // Update goals for the user
        List<GoalProgressUpdateRequest> goalProgressUpdateRequests = testResultResponse.getAnswerResponses()
                .stream()
                .map(answerResponse -> GoalProgressUpdateRequest.builder()
                        .part(answerResponse.getPart())
                        .timeSpent(answerResponse.getTimeSpent())
                        .build())
                .toList();
        goalService.updateGoalProgressAfterTest(test.getUser().getEmail(), goalProgressUpdateRequests);
    }

    @Override
    public void saveUncompletedTest(String email, List<UncompletedAnswerRequest> uncompletedAnswerRequests) {
        Test test = new Test();
        test.setScore(0); // Set initial score to 0 for uncompleted tests
        test.setUser(userService.findByEmail(email));
        test.setCreatedAt(LocalDateTime.now());

        // Lưu trước để có ID cho liên kết
        testRepository.save(test);

        // Tạo danh sách Answer từ AnswerRequest
        List<Part> parts = new ArrayList<>();
        for (UncompletedAnswerRequest uncompletedAnswerRequest : uncompletedAnswerRequests) {
            Answer answer = new Answer();
            answer.setAnswer(uncompletedAnswerRequest.getUserAnswer());
            answer.setTimeSpent(uncompletedAnswerRequest.getTimeSpent());
            answer.setQuestion(questionService.findById(uncompletedAnswerRequest.getQuestionId()).orElse(null));
            answer.setTest(test);
            // Lưu các Answer
            answerService.save(answer);

            // Lưu Part nếu chưa có
            Part part = partService.findByName(uncompletedAnswerRequest.getPart());
            if (part != null && !parts.contains(part)) {
                parts.add(part);
            }
        }

        // Lưu các Part liên kết với Test
        for (Part part : parts) {
            TestPart testPart = new TestPart();
            testPart.setTest(test);
            testPart.setPart(part);
            testPartService.save(testPart);
        }

        // Update study streak for the user
        studyStreakService.updateCurrentStreak(email);

        // Update goals for the user
        List<GoalProgressUpdateRequest> goalProgressUpdateRequests = uncompletedAnswerRequests
                .stream()
                .map(answerResponse -> GoalProgressUpdateRequest.builder()
                        .part(answerResponse.getPart())
                        .timeSpent(answerResponse.getTimeSpent())
                        .build())
                .toList();
        goalService.updateGoalProgressAfterTest(email, goalProgressUpdateRequests);
    }

    @Override
    public void saveUncompletedTestById(Long testId, List<UncompletedAnswerRequest> uncompletedAnswerRequests) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new ResourceNotFoundException("Test", "id", testId));

        // Xóa các Answer cũ nếu có
        answerService.deleteAllByTestId(testId);

        // Tạo danh sách Answer mới
        List<Answer> newAnswers = uncompletedAnswerRequests.stream()
                .map(req -> {
                    Answer answer = new Answer();
                    answer.setAnswer(req.getUserAnswer());
                    answer.setTimeSpent(req.getTimeSpent());
                    answer.setQuestion(questionService.findById(req.getQuestionId()).orElse(null));
                    answer.setTest(test);
                    return answer;
                })
                .toList();
        answerService.saveAll(newAnswers);

        // Update study streak for the user
        studyStreakService.updateCurrentStreak(test.getUser().getEmail());

        // Update goals for the user
        List<GoalProgressUpdateRequest> goalProgressUpdateRequests = uncompletedAnswerRequests
                .stream()
                .map(answerResponse -> GoalProgressUpdateRequest.builder()
                        .part(answerResponse.getPart())
                        .timeSpent(answerResponse.getTimeSpent())
                        .build())
                .toList();
        goalService.updateGoalProgressAfterTest(test.getUser().getEmail(), goalProgressUpdateRequests);
    }

    @Override
    public TestStatisticResponse calculateTestStatistic(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        List<Test> tests = testRepository.findAllByUser(user);

        if (tests.isEmpty()) {
            return TestStatisticResponse.builder()
                    .totalTests(0)
                    .averageScore(0)
                    .highestScore(0)
                    .totalTimeSpent(0)
                    .build();
        }

        int totalTests = tests.size();

        // Calculate average score percentage
        double totalScorePercentage = tests.stream()
                .mapToDouble(test -> {
                    if (test.getAnswers() == null || test.getAnswers().isEmpty()) {
                        return 0.0;
                    }
                    return (test.getScore() * 100.0) / test.getAnswers().size();
                })
                .sum();
        int averageScore = (int) Math.round(totalScorePercentage / totalTests);

        // Calculate highest score percentage
        int highestScore = tests.stream()
                .mapToInt(test -> {
                    if (test.getAnswers() == null || test.getAnswers().isEmpty()) {
                        return 0;
                    }
                    return (int) ((test.getScore() * 100.0) / test.getAnswers().size());
                })
                .max()
                .orElse(0);

        // Calculate total time spent (in minutes)
        int totalTimeSeconds = tests.stream()
                .flatMap(t -> t.getAnswers().stream())
                .mapToInt(answer -> answer.getTimeSpent() != null ? answer.getTimeSpent() : 0)
                .sum();

        int totalTimeMinutes = totalTimeSeconds / 60;

        return TestStatisticResponse.builder()
                .totalTests(totalTests)
                .averageScore(averageScore)
                .highestScore(highestScore)
                .totalTimeSpent(totalTimeMinutes)
                .build();
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Ho_Chi_Minh")
    public void deleteExpiredTests() {
        List<Long> idsToDelete = testRepository.getUncompletedTestIdsCreatedWithinLast7Days(LocalDateTime.now().minusDays(7));
        if (!idsToDelete.isEmpty()) {
            List<Test> testsToDelete = testRepository.findAllById(idsToDelete);
            testRepository.deleteAll(testsToDelete);
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

    private RecentTestResponse mapToRecentTestResponse(Test test) {
        return RecentTestResponse.builder()
                .testId(test.getId())
                .createdAt(test.getCreatedAt().toString())
                .totalAnswers(test.getAnswers() != null ? test.getAnswers().size() : 0)
                .score(test.getScore())
                .build();
    }
}
