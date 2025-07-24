package intern.nhhtuan.toeic_mentor.controller.admin;

import intern.nhhtuan.toeic_mentor.dto.response.ChatbotRatingDetailResponse;
import intern.nhhtuan.toeic_mentor.dto.response.ChatbotRatingResponse;
import intern.nhhtuan.toeic_mentor.entity.enums.EChatbotRating;
import intern.nhhtuan.toeic_mentor.service.interfaces.IChatbotRatingService;
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

@Controller(value = "adminRatingController")
@RequestMapping("/admin/ratings")
@RequiredArgsConstructor
public class RatingController {
    private final IChatbotRatingService chatbotRatingService;

    @GetMapping("/chatbot-ratings")
    public String getChatbotFeedbacks(
            Model model,
            @RequestParam(required = false) EChatbotRating rating,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAtEnd,
            @RequestParam(required = false) String userEmail,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        Page<ChatbotRatingResponse> ratings = chatbotRatingService.getChatbotRatings(
                rating, createdAtStart, createdAtEnd, userEmail, page, size, sortBy, direction
        );
        model.addAttribute("ratings", ratings);
        model.addAttribute("numberLikeRatings", chatbotRatingService.countLikeRating());
        model.addAttribute("numberDislikeRatings", chatbotRatingService.countDislikeRating());
        return "admin/rating/chatbot-rating-list";
    }

    @GetMapping("/chatbot-ratings/{id}")
    public String getChatbotFeedbackDetail(@PathVariable Long id, Model model) {
        ChatbotRatingDetailResponse rating = chatbotRatingService.getChatbotRatingById(id);
        if (rating == null) {
            return "redirect:/admin/ratings/chatbot-ratings";
        }
        model.addAttribute("rating", rating);
        return "admin/rating/chatbot-rating-detail";
    }

}
