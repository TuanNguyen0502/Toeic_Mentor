package intern.nhhtuan.toeic_mentor.repository.specification;

import intern.nhhtuan.toeic_mentor.entity.ChatbotRating;
import intern.nhhtuan.toeic_mentor.entity.enums.EChatbotRating;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class ChatbotRatingSpecification {
    public static Specification<ChatbotRating> hasRating(EChatbotRating rating) {
        return (root, query, criteriaBuilder) ->
                rating == null ? null : criteriaBuilder.equal(root.get("rating"), rating);
    }

    public static Specification<ChatbotRating> createdAtBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, criteriaBuilder) -> {
            if (start != null && end != null) {
                return criteriaBuilder.between(root.get("createdAt"), start, end);
            } else if (start != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), start);
            } else if (end != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), end);
            } else {
                return null;
            }
        };
    }

    public static Specification<ChatbotRating> hasUser(Long userId) {
        return (root, query, criteriaBuilder) ->
                userId == null ? null : criteriaBuilder.equal(root.get("user").get("id"), userId);
    }
}
