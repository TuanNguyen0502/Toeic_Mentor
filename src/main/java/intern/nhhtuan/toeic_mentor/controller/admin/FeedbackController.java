package intern.nhhtuan.toeic_mentor.controller.admin;

import intern.nhhtuan.toeic_mentor.dto.response.ChatbotFeedbackResponse;
import intern.nhhtuan.toeic_mentor.entity.enums.EChatbotFeedback;
import intern.nhhtuan.toeic_mentor.service.interfaces.IChatbotFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller(value = "adminFeedbackController")
@RequestMapping("/admin/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {
    private final IChatbotFeedbackService chatbotFeedbackService;

    @GetMapping
    public String getFeedbacks(
            Model model,
            @RequestParam(required = false) EChatbotFeedback feedback,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAtEnd,
            @RequestParam(required = false) String userEmail,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        Page<ChatbotFeedbackResponse> feedbacks = chatbotFeedbackService.getFeedbacks(
                feedback, createdAtStart, createdAtEnd, userEmail, page, size, sortBy, direction
        );
        model.addAttribute("feedbacks", feedbacks);
        return "admin/feedback/feedback-list";
    }
}
