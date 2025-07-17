package intern.nhhtuan.toeic_mentor.controller.admin;

import intern.nhhtuan.toeic_mentor.dto.response.ChatbotFeedbackDetailResponse;
import intern.nhhtuan.toeic_mentor.dto.response.ChatbotFeedbackResponse;
import intern.nhhtuan.toeic_mentor.entity.enums.EChatbotFeedback;
import intern.nhhtuan.toeic_mentor.service.interfaces.IChatbotFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller(value = "adminFeedbackController")
@RequestMapping("/admin/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {
    private final IChatbotFeedbackService chatbotFeedbackService;

    @GetMapping("/chatbot-feedbacks")
    public String getChatbotFeedbacks(
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
        return "admin/feedback/chatbot-feedback-list";
    }

    @GetMapping("/chatbot-feedbacks/{id}")
    public String getChatbotFeedbackDetail(@PathVariable Long id, Model model) {
        ChatbotFeedbackDetailResponse feedback = chatbotFeedbackService.getChatbotFeedbackById(id);
        if (feedback == null) {
            return "redirect:/admin/feedbacks/chatbot-feedbacks";
        }
        model.addAttribute("feedback", feedback);
        return "admin/feedback/chatbot-feedback-detail";
    }

}
