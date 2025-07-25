package intern.nhhtuan.toeic_mentor.controller.user;

import intern.nhhtuan.toeic_mentor.dto.request.ChatbotRatingRequest;
import intern.nhhtuan.toeic_mentor.service.interfaces.IChatbotRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
public class RatingController {
    private final IChatbotRatingService chatbotRatingService;

    @PostMapping("/chatbot-rate")
    public ResponseEntity<String> rateMessage(@RequestBody ChatbotRatingRequest chatbotRatingRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication != null ? authentication.getName() : "anonymous";
        try {
            chatbotRatingService.saveRating(chatbotRatingRequest, userEmail);
            return ResponseEntity.ok("Message rated successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to rate message: " + e.getMessage());
        }
    }
}
