package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.request.ChatbotFeedbackRequest;
import intern.nhhtuan.toeic_mentor.dto.response.ChatbotFeedbackDetailResponse;
import intern.nhhtuan.toeic_mentor.dto.response.ChatbotFeedbackResponse;
import intern.nhhtuan.toeic_mentor.entity.enums.EChatbotFeedback;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface IChatbotFeedbackService {
    void saveFeedback(ChatbotFeedbackRequest feedback, String userEmail);

    Page<ChatbotFeedbackResponse> getFeedbacks(
            EChatbotFeedback feedback,
            LocalDateTime createdAtStart,
            LocalDateTime createdAtEnd,
            String userEmail,
            int page,
            int size,
            String sortBy,
            String direction
    );

    ChatbotFeedbackDetailResponse getChatbotFeedbackById(Long id);
}
