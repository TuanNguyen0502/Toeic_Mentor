package intern.nhhtuan.toeic_mentor.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import intern.nhhtuan.toeic_mentor.dto.response.QuestionResponse;
import intern.nhhtuan.toeic_mentor.service.implement.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/stream")
    public Flux<String> chatWithStream(@RequestParam String message,
                                       @RequestParam String email) {
        return chatService.getChatResponse(message, email);
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
}