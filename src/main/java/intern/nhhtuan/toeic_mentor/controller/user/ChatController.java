package intern.nhhtuan.toeic_mentor.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import intern.nhhtuan.toeic_mentor.dto.response.QuestionResponse;
import intern.nhhtuan.toeic_mentor.service.interfaces.IChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final IChatService chatService;

    @GetMapping("/stream")
    public Flux<String> chatWithStream(@RequestParam String message,
                                       @RequestParam String conversationId) {
        return chatService.getChatResponse(message, conversationId);
    }

    @PostMapping("/submit-test")
    public Flux<String> submitTest(@RequestBody List<QuestionResponse> questions) throws JsonProcessingException {
        // Build the prompt for TOEIC analysis based on the submitted questions
        String prompt = chatService.buildToeicAnalysisPrompt(questions);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Determine the email of the authenticated user or use "anonymous" if not authenticated
        String email = authentication != null && authentication.isAuthenticated() ? authentication.getName() : "anonymous";
        return chatService.getChatResponse(prompt, email);
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

    @GetMapping("/conversation-name")
    public String generateConversationName(@RequestParam String message) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Determine the email of the authenticated user or use "anonymous" if not authenticated
        String email = authentication != null && authentication.isAuthenticated() ? authentication.getName() : "anonymous";
        return chatService.generateConversationId(message, email);
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