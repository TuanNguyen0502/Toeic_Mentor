package intern.nhhtuan.toeic_mentor.controller.user;

import intern.nhhtuan.toeic_mentor.dto.request.AnswerRequest;
import intern.nhhtuan.toeic_mentor.dto.response.TestResultResponse;
import intern.nhhtuan.toeic_mentor.service.interfaces.IChatService;
import intern.nhhtuan.toeic_mentor.service.interfaces.ITestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
public class TestController {
    private final IChatService chatService;
    private final ITestService testService;

    @PostMapping("/results")
    public TestResultResponse submitTest(@RequestBody List<AnswerRequest> answerRequests) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Determine the email of the authenticated user or use "anonymous" if not authenticated
        String email = authentication != null && authentication.isAuthenticated() ? authentication.getName() : "anonymous";

        TestResultResponse testResultResponse = chatService.analyzeTestResult(answerRequests);
        // Save the test results to the database
//        testService.saveTest(email, testResultResponse);
        return testResultResponse;
    }
}
