package intern.nhhtuan.toeic_mentor.controller.user;

import intern.nhhtuan.toeic_mentor.dto.request.AnswerExplanationRequest;
import intern.nhhtuan.toeic_mentor.dto.request.AnswerRequest;
import intern.nhhtuan.toeic_mentor.dto.request.UncompletedAnswerRequest;
import intern.nhhtuan.toeic_mentor.dto.response.AnswerExplanationResponse;
import intern.nhhtuan.toeic_mentor.dto.response.RecentTestResponse;
import intern.nhhtuan.toeic_mentor.dto.response.TestResultResponse;
import intern.nhhtuan.toeic_mentor.dto.response.TestStatisticResponse;
import intern.nhhtuan.toeic_mentor.service.interfaces.IChatService;
import intern.nhhtuan.toeic_mentor.service.interfaces.IPdfService;
import intern.nhhtuan.toeic_mentor.service.interfaces.ITestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Slf4j(topic = "TestController")
@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
public class TestController {
    private final IChatService chatService;
    private final ITestService testService;
    private final IPdfService pdfService;

    @PostMapping(value = "/chat", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<AnswerExplanationResponse> chatWithQuestion(@Valid @RequestBody AnswerExplanationRequest answerExplanationRequest) {
        log.info(answerExplanationRequest.toString());
        return chatService.getChatResponse(answerExplanationRequest.getMessage(), answerExplanationRequest.getConversationId() , answerExplanationRequest.getAnswerId());
    }

    @PostMapping("/results")
    public TestResultResponse submitTest(@RequestBody List<AnswerRequest> answerRequests) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Determine the email of the authenticated user or use "anonymous" if not authenticated
        String email = authentication != null && authentication.isAuthenticated() ? authentication.getName() : "anonymous";

        TestResultResponse testResultResponse = chatService.analyzeTestResult(answerRequests);
        // Save the test results to the database and get the saved test
        testService.saveTest(email, testResultResponse);

        return testResultResponse;
    }

    @PostMapping("/results/{testId}")
    public TestResultResponse submitTest(@PathVariable Long testId, @RequestBody List<AnswerRequest> answerRequests) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Determine the email of the authenticated user or use "anonymous" if not authenticated
        String email = authentication != null && authentication.isAuthenticated() ? authentication.getName() : "anonymous";

        TestResultResponse testResultResponse = chatService.analyzeTestResult(answerRequests);
        // Save the test results to the database with the existing testId
        testService.saveTestById(testId, testResultResponse);

        return testResultResponse;
    }

    @GetMapping("/{testId}/pdf")
    public ResponseEntity<byte[]> downloadTestResultPdf(@PathVariable Long testId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication != null && authentication.isAuthenticated() ? authentication.getName() : "anonymous";

            // Get test result from database by testId
            TestResultResponse testResultResponse = testService.getTestResult(testId, email);
            ByteArrayOutputStream pdfStream = pdfService.generateTestResultPdf(testResultResponse);
            
            // Generate filename with test ID
            String filename = String.format("toeic_test_result_%d.pdf", testId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(pdfStream.size());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfStream.toByteArray());
                    
        } catch (Exception e) {
            log.error("Error generating PDF for test ID {}: {}", testId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/recent-tests")
    public List<RecentTestResponse> getRecentTests(@RequestParam(value = "number", defaultValue = "10") int number) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Determine the email of the authenticated user or use "anonymous" if not authenticated
        String email = authentication != null && authentication.isAuthenticated() ? authentication.getName() : "anonymous";
        return testService.getRecentTests(email, number);
    }

    @GetMapping("/statistics")
    public ResponseEntity<TestStatisticResponse> getTestStatistics() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication != null && authentication.isAuthenticated() ? authentication.getName() : "anonymous";
        return ResponseEntity.ok(testService.calculateTestStatistic(email));
    }

    @PostMapping("/uncompleted-test")
    public ResponseEntity<Void> saveUncompletedTest(@RequestBody List<UncompletedAnswerRequest> uncompletedAnswerRequests) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication != null && authentication.isAuthenticated() ? authentication.getName() : "anonymous";
        testService.saveUncompletedTest(email, uncompletedAnswerRequests);
        return ResponseEntity.ok().build();
    }
}
