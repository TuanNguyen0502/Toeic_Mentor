package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.request.ChatbotFeedbackRequest;

public interface IChatbotFeedbackService {
    boolean saveFeedback(ChatbotFeedbackRequest feedback, String userEmail);
}
