package intern.nhhtuan.toeic_mentor.controller.user;

import intern.nhhtuan.toeic_mentor.dto.request.TestRequest;
import intern.nhhtuan.toeic_mentor.dto.response.QuestionResponse;
import intern.nhhtuan.toeic_mentor.service.interfaces.IQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final IQuestionService questionService;

    @GetMapping("")
    public String index() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Determine the email of the authenticated user or use "anonymous" if not authenticated
        String conversationId = authentication != null && authentication.isAuthenticated() ? authentication.getName() : "anonymous";
        return "redirect:/chat/" + conversationId;
    }

    @GetMapping("/{conversationId}")
    public String chatbot(@PathVariable String conversationId, Model model) {
        // Ensure the conversationId contains the user's email to prevent unauthorized access
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Determine the email of the authenticated user or use "anonymous" if not authenticated
        String email = authentication != null && authentication.isAuthenticated() ? authentication.getName() : "anonymous";
        if (!conversationId.contains(email)) {
            return "redirect:/chat/" + email;
        }
        model.addAttribute("conversationId", conversationId);
        return "user/chat";
    }

    @PostMapping("/generate-test")
    public String generateTest(@RequestBody TestRequest request, Model model) {
        List<QuestionResponse> questions = questionService.generateTest(request);
        model.addAttribute("questions", questions);
        return "user/take-test";
    }
}
