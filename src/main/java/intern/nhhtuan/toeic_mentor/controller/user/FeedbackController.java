package intern.nhhtuan.toeic_mentor.controller.user;

import intern.nhhtuan.toeic_mentor.dto.request.ChatbotFeedbackRequest;
import intern.nhhtuan.toeic_mentor.service.interfaces.IChatbotFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {
    private final IChatbotFeedbackService chatbotFeedbackService;

    @PostMapping("/chatbot")
    public String submitChatbotFeedback(@ModelAttribute ChatbotFeedbackRequest feedback) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Assuming the user's email is stored in the authentication object
        try {
            chatbotFeedbackService.saveFeedback(feedback, userEmail);
        } catch (Exception e) {
            // Handle the exception, e.g., log it or return an error response
            return "Error saving feedback: " + e.getMessage();
        }
        // Logic to handle chatbot feedback
        return "Chatbot feedback received";
    }
}
