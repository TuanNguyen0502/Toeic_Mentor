package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.request.ChatbotFeedbackRequest;
import intern.nhhtuan.toeic_mentor.entity.ChatMemory;
import intern.nhhtuan.toeic_mentor.entity.ChatbotFeedback;
import intern.nhhtuan.toeic_mentor.entity.User;
import intern.nhhtuan.toeic_mentor.repository.ChatMemoryRepository;
import intern.nhhtuan.toeic_mentor.repository.ChatbotFeedbackRepository;
import intern.nhhtuan.toeic_mentor.repository.UserRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.IChatbotFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class IChatbotFeedbackServiceImpl implements IChatbotFeedbackService {
    private final ChatbotFeedbackRepository chatbotFeedbackRepository;
    private final UserRepository userRepository;
    private final ChatMemoryRepository chatMemoryRepository;

    @Override
    public boolean saveFeedback(ChatbotFeedbackRequest feedback, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + userEmail));
        ChatMemory chatMemory = chatMemoryRepository.findById(feedback.getChatbotResponseId())
                .orElseThrow(() -> new IllegalArgumentException("Chat memory not found with ID: " + feedback.getChatbotResponseId()));
        // Create a new ChatbotFeedback entity and set its properties
        ChatbotFeedback chatbotFeedback = new ChatbotFeedback();
        chatbotFeedback.setFeedback(feedback.getFeedback());
        chatbotFeedback.setCreatedAt(LocalDateTime.now());
        chatbotFeedback.setUser(user);
        chatbotFeedback.setChatMemory(chatMemory);
        // Save the feedback to the repository
        chatbotFeedbackRepository.save(chatbotFeedback);
        return true; // Return true if saved successfully, false otherwise
    }
}
