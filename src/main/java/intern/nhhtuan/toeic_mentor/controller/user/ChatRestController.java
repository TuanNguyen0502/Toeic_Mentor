package intern.nhhtuan.toeic_mentor.controller.user;

import intern.nhhtuan.toeic_mentor.dto.request.AnswerRequest;
import intern.nhhtuan.toeic_mentor.dto.response.TestResultResponse;
import intern.nhhtuan.toeic_mentor.service.interfaces.IChatService;
import intern.nhhtuan.toeic_mentor.service.interfaces.ITestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatRestController {
    private final IChatService chatService;
    private final ITestService testService;

    @PostMapping("/stream")
    public Flux<String> chatWithStream(@RequestParam String message,
                                       @RequestParam(required = false) String conversationId,
                                       @RequestParam(value = "image", required = false) MultipartFile image) {
        if (conversationId == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication != null && authentication.isAuthenticated() ? authentication.getName() : "anonymous";
            conversationId = chatService.generateConversationId(message, email);
        }

        final String finalConversationId = conversationId;
        Flux<String> response;

        if (image == null || image.isEmpty()) {
            response = chatService.getChatResponse(message, finalConversationId);
        } else {
            try (InputStream inputStream = image.getInputStream()) {
                response = chatService.getChatResponse(message, finalConversationId, inputStream, image.getContentType());
            } catch (Exception e) {
                response = Flux.error(new RuntimeException("Error processing image input stream", e));
            }
        }

        // Send conversationId as first message
        return Flux.concat(
                Flux.just("ConversationId: " + finalConversationId + "\n"),
                response
        );
    }

    @PostMapping("/submit-test")
    public TestResultResponse submitTest(@RequestBody List<AnswerRequest> answerRequests) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Determine the email of the authenticated user or use "anonymous" if not authenticated
        String email = authentication != null && authentication.isAuthenticated() ? authentication.getName() : "anonymous";

        TestResultResponse testResultResponse = chatService.analyzeTestResult(answerRequests);
        // Save the test results to the database
//        testService.saveTest(email, testResultResponse);
        return testResultResponse;
    }

    @GetMapping("/conversation-ids")
    public List<String> getConversationIds(@RequestParam String email) {
        return chatService.getConversationIdsByEmail(email);
    }

    @GetMapping("/conversation")
    public List<String> getChatHistory(@RequestParam String conversationId) {
        return chatService.getChatHistory(conversationId);
    }

    @DeleteMapping("/conversation")
    public void deleteConversation(@RequestParam String conversationId) {
        chatService.deleteByConversationId(conversationId);
    }

    @PutMapping("/conversation-name")
    public String updateConversationName(@RequestParam String conversationId, @RequestParam String newName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Determine the email of the authenticated user or use "anonymous" if not authenticated
        String email = authentication != null && authentication.isAuthenticated() ? authentication.getName() : "anonymous";
        String newConversationId = email + "_" + newName;
        boolean result = chatService.renameConversation(conversationId, newConversationId);
        if (result) {
            return newConversationId;
        } else {
            return "Failed to update conversation name. Please check the conversation.";
        }
    }
}
