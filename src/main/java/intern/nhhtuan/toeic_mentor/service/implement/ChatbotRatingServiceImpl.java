package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.request.ChatbotRatingRequest;
import intern.nhhtuan.toeic_mentor.dto.response.ChatbotRatingDetailResponse;
import intern.nhhtuan.toeic_mentor.dto.response.ChatbotRatingResponse;
import intern.nhhtuan.toeic_mentor.dto.response.ChatbotResponse;
import intern.nhhtuan.toeic_mentor.entity.ChatbotRating;
import intern.nhhtuan.toeic_mentor.entity.RatableMessage;
import intern.nhhtuan.toeic_mentor.entity.User;
import intern.nhhtuan.toeic_mentor.entity.enums.EChatbotRating;
import intern.nhhtuan.toeic_mentor.repository.ChatbotRatingRepository;
import intern.nhhtuan.toeic_mentor.repository.RatingEnabledChatMemoryRepository;
import intern.nhhtuan.toeic_mentor.repository.UserRepository;
import intern.nhhtuan.toeic_mentor.repository.specification.ChatbotRatingSpecification;
import intern.nhhtuan.toeic_mentor.service.interfaces.IChatbotRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatbotRatingServiceImpl implements IChatbotRatingService {
    private final ChatbotRatingRepository chatbotRatingRepository;
    private final UserRepository userRepository;
    private final RatingEnabledChatMemoryRepository ratingEnabledChatMemoryRepository;

    @Override
    public void saveRating(ChatbotRatingRequest ratingRequest, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + userEmail));
        if (!ratingEnabledChatMemoryRepository.existsByMessageId(ratingRequest.getMessageId())) {
            throw new IllegalArgumentException("Chat memory not found with message ID: " + ratingRequest.getMessageId());
        }

        // Create a new ChatbotRating entity and set its properties
        ChatbotRating chatbotRating = new ChatbotRating();
        chatbotRating.setRating(ratingRequest.getRating());
        chatbotRating.setCreatedAt(LocalDateTime.now());
        chatbotRating.setUser(user);
        chatbotRating.setMessageId(ratingRequest.getMessageId());
        chatbotRating.setMessage(ratingEnabledChatMemoryRepository
                .getMessageById(ratingRequest.getMessageId()).getText());
        // Save the feedback to the repository
        chatbotRatingRepository.save(chatbotRating);
    }

    @Override
    public Page<ChatbotRatingResponse> getChatbotRatings(
            EChatbotRating rating,
            LocalDateTime createdAtStart,
            LocalDateTime createdAtEnd,
            String userEmail,
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        Specification<ChatbotRating> spec = (root, query, cb) -> cb.conjunction(); // Bắt đầu với 1 điều kiện TRUE

        if (rating != null) {
            spec = spec.and(ChatbotRatingSpecification.hasRating(rating));
        }
        if (createdAtStart != null || createdAtEnd != null) {
            spec = spec.and(ChatbotRatingSpecification.createdAtBetween(createdAtStart, createdAtEnd));
        }
        if (userEmail != null) {
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + userEmail));
            Long userId = user.getId();
            spec = spec.and(ChatbotRatingSpecification.hasUser(userId));
        }

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return chatbotRatingRepository.findAll(spec, pageable)
                .map(feedbackEntity -> {
                            String conversationId = feedbackEntity.getMessageId() != null
                                    ? ratingEnabledChatMemoryRepository
                                    .getConversationIdByMessageId(feedbackEntity.getMessageId())
                                    : null;
                            return new ChatbotRatingResponse(
                                    feedbackEntity.getId(),
                                    feedbackEntity.getUser().getEmail(),
                                    getConversationTitle(conversationId),
                                    feedbackEntity.getRating().name(),
                                    feedbackEntity.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
                            );
                        }
                );
    }

    @Override
    public ChatbotRatingDetailResponse getChatbotRatingById(Long id) {
        ChatbotRating rating = chatbotRatingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chatbot rating not found with ID: " + id));

        RatableMessage ratableMessage = rating.getMessageId() != null
                ? ratingEnabledChatMemoryRepository.getMessageById(rating.getMessageId())
                : null;

        String conversationTitle = "Unknown Or Deleted Conversation";
        String chatbotResponseCreatedAt = "Unknown Or Deleted Conversation";
        List<ChatbotResponse> chatbotResponses = new ArrayList<>();

        if (ratableMessage != null) {
            conversationTitle = getConversationTitle(ratableMessage.getConversationId());
            chatbotResponseCreatedAt = ratingEnabledChatMemoryRepository
                    .getCreatedAtByMessageId(rating.getMessageId());
            chatbotResponses = ratingEnabledChatMemoryRepository.getChatHistory(ratableMessage.getConversationId());
            System.out.println(chatbotResponses.size());
        }

        return ChatbotRatingDetailResponse.builder()
                .id(rating.getId())
                .userEmail(rating.getUser().getEmail())
                .messageId(rating.getMessageId())
                .content(rating.getMessage())
                .conversationTitle(conversationTitle)
                .chatbotResponseCreatedAt(chatbotResponseCreatedAt)
                .rating(rating.getRating().name())
                .ratedAt(rating.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
                .chatbotResponses(chatbotResponses)
                .build();
    }

    @Override
    public int countLikeRating() {
        return chatbotRatingRepository.countByRating(EChatbotRating.LIKE);
    }

    @Override
    public int countDislikeRating() {
        return chatbotRatingRepository.countByRating(EChatbotRating.DISLIKE);
    }

    private String getConversationTitle(String conversationId) {
        if (conversationId == null || conversationId.isEmpty()) {
            return "Unknown Or Deleted Conversation";
        }

        // Split the conversationId to extract the title
        // Assuming the format is "conversationId_title" or just "title"
        // If the conversationId does not contain an underscore, it is treated as the title
        if (!conversationId.contains("_")) {
            return conversationId.replaceAll("_", " ");
        }
        // If it contains an underscore, split and return the second part as the title
        // If there is no second part, return the first part as the title
        String[] conversationIds = conversationId.split("_");
        if (conversationIds.length < 2) {
            return conversationIds[0].replaceAll("_", " ");
        } else {
            return conversationIds[1].replaceAll("_", " ");
        }
    }
}
