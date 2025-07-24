package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.request.ChatbotRatingRequest;
import intern.nhhtuan.toeic_mentor.dto.response.ChatbotRatingDetailResponse;
import intern.nhhtuan.toeic_mentor.dto.response.ChatbotRatingResponse;
import intern.nhhtuan.toeic_mentor.entity.enums.EChatbotRating;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface IChatbotRatingService {
    void saveRating(ChatbotRatingRequest ratingRequest, String userEmail);

    Page<ChatbotRatingResponse> getChatbotRatings(
            EChatbotRating rating,
            LocalDateTime createdAtStart,
            LocalDateTime createdAtEnd,
            String userEmail,
            int page,
            int size,
            String sortBy,
            String direction
    );

    ChatbotRatingDetailResponse getChatbotRatingById(Long id);

    int countLikeRating();

    int countDislikeRating();
}
