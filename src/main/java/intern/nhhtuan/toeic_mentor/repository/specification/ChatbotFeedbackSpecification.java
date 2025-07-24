package intern.nhhtuan.toeic_mentor.repository.specification;

import intern.nhhtuan.toeic_mentor.entity.ChatbotFeedback;
import intern.nhhtuan.toeic_mentor.entity.enums.EChatbotFeedback;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class ChatbotFeedbackSpecification {
    public static Specification<ChatbotFeedback> hasFeedback(EChatbotFeedback feedback) {
        return (root, query, criteriaBuilder) ->
                feedback == null ? null : criteriaBuilder.equal(root.get("feedback"), feedback);
    }

    public static Specification<ChatbotFeedback> createdAtBetween(LocalDateTime start, LocalDateTime end) {
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

    public static Specification<ChatbotFeedback> hasUser(Long userId) {
        return (root, query, criteriaBuilder) ->
                userId == null ? null : criteriaBuilder.equal(root.get("user").get("id"), userId);
    }
}
