package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.request.ChatbotFeedbackRequest;
import intern.nhhtuan.toeic_mentor.dto.response.ChatbotFeedbackDetailResponse;
import intern.nhhtuan.toeic_mentor.dto.response.ChatbotFeedbackResponse;
import intern.nhhtuan.toeic_mentor.entity.ChatMemory;
import intern.nhhtuan.toeic_mentor.entity.ChatbotFeedback;
import intern.nhhtuan.toeic_mentor.entity.User;
import intern.nhhtuan.toeic_mentor.entity.enums.EChatbotFeedback;
import intern.nhhtuan.toeic_mentor.repository.ChatMemoryRepository;
import intern.nhhtuan.toeic_mentor.repository.ChatbotFeedbackRepository;
import intern.nhhtuan.toeic_mentor.repository.UserRepository;
import intern.nhhtuan.toeic_mentor.repository.specification.ChatbotFeedbackSpecification;
import intern.nhhtuan.toeic_mentor.service.interfaces.IChatbotFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ChatbotFeedbackServiceImpl implements IChatbotFeedbackService {
    private final ChatbotFeedbackRepository chatbotFeedbackRepository;
    private final UserRepository userRepository;
    private final ChatMemoryRepository chatMemoryRepository;

    @Override
    public void saveFeedback(ChatbotFeedbackRequest feedback, String userEmail) {
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
    }

    @Override
    public Page<ChatbotFeedbackResponse> getFeedbacks(
            EChatbotFeedback feedback,
            LocalDateTime createdAtStart,
            LocalDateTime createdAtEnd,
            String userEmail,
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        Specification<ChatbotFeedback> spec = (root, query, cb) -> cb.conjunction(); // Bắt đầu với 1 điều kiện TRUE

        if (feedback != null) {
            spec = spec.and(ChatbotFeedbackSpecification.hasFeedback(feedback));
        }
        if (createdAtStart != null || createdAtEnd != null) {
            spec = spec.and(ChatbotFeedbackSpecification.createdAtBetween(createdAtStart, createdAtEnd));
        }
        if (userEmail != null) {
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + userEmail));
            Long userId = user.getId();
            spec = spec.and(ChatbotFeedbackSpecification.hasUser(userId));
        }

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return chatbotFeedbackRepository.findAll(spec, pageable)
                .map(feedbackEntity -> new ChatbotFeedbackResponse(
                        feedbackEntity.getId(),
                        feedbackEntity.getUser().getEmail(),
                        feedbackEntity.getChatMemory().getConversationId().split("_")[1].replaceAll("_", " "),
                        feedbackEntity.getFeedback().name(),
                        feedbackEntity.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
                ));
    }

    @Override
    public ChatbotFeedbackDetailResponse getChatbotFeedbackById(Long id) {
        ChatbotFeedback feedback = chatbotFeedbackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chatbot feedback not found with ID: " + id));

        ChatMemory chatMemory = feedback.getChatMemory();
        String conversationName = chatMemory.getConversationId().split("_")[1].replaceAll("_", " ");

        return new ChatbotFeedbackDetailResponse(
                feedback.getId(),
                feedback.getUser().getEmail(),
                chatMemory.getId(),
                conversationName,
                chatMemory.getContent(),
                chatMemory.getTimestamp().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                feedback.getFeedback().name(),
                feedback.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
        );
    }
}
