package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.request.ChatbotRatingRequest;
import intern.nhhtuan.toeic_mentor.dto.response.ChatbotRatingDetailResponse;
import intern.nhhtuan.toeic_mentor.dto.response.ChatbotRatingResponse;
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
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ChatbotRatingServiceImpl implements IChatbotRatingService {
    private final ChatbotRatingRepository chatbotRatingRepository;
    private final UserRepository userRepository;
    private final RatingEnabledChatMemoryRepository ratingEnabledChatMemoryRepository;

    @Override
    public void saveFeedback(ChatbotRatingRequest ratingRequest, String userEmail) {
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
                            String[] conversationIds = ratingEnabledChatMemoryRepository
                                    .getConversationIdByMessageId(feedbackEntity.getMessageId())
                                    .split("_");
                            String conversationId;
                            if (conversationIds.length < 2) {
                                conversationId = conversationIds[0].replaceAll("_", " ");
                            } else {
                                conversationId = conversationIds[1].replaceAll("_", " ");
                            }
                            return new ChatbotRatingResponse(
                                    feedbackEntity.getId(),
                                    feedbackEntity.getUser().getEmail(),
                                    conversationId,
                                    feedbackEntity.getRating().name(),
                                    feedbackEntity.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
                            );
                        }
                );
    }

    @Override
    public ChatbotRatingDetailResponse getChatbotFeedbackById(Long id) {
        ChatbotRating rating = chatbotRatingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chatbot rating not found with ID: " + id));

        RatableMessage ratableMessage = ratingEnabledChatMemoryRepository.getMessageById(rating.getMessageId());
        if (ratableMessage == null) {
            throw new IllegalArgumentException("Chat memory not found with message ID: " + rating.getMessageId());
        }

        String conversationTitle = ratableMessage.getConversationId().split("_")[1].replaceAll("_", " ");

        return ChatbotRatingDetailResponse.builder()
                .id(rating.getId())
                .userEmail(rating.getUser().getEmail())
                .messageId(rating.getMessageId())
                .content(ratableMessage.getText())
                .conversationTitle(conversationTitle)
                .chatbotResponseCreatedAt(ratingEnabledChatMemoryRepository.getCreatedAtByMessageId(ratableMessage.getMessageId()))
                .rating(rating.getRating().name())
                .ratedAt(rating.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
                .build();
    }

    @Override
    public int countLikeFeedback() {
        return chatbotRatingRepository.countByRating(EChatbotRating.LIKE);
    }

    @Override
    public int countDislikeFeedback() {
        return chatbotRatingRepository.countByRating(EChatbotRating.DISLIKE);
    }
}
