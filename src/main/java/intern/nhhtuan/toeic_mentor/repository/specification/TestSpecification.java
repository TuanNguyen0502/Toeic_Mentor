package intern.nhhtuan.toeic_mentor.repository.specification;

import intern.nhhtuan.toeic_mentor.entity.Test;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TestSpecification {
    public static Specification<Test> createdAtBetween(LocalDateTime start, LocalDateTime end) {
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

    public static Specification<Test> belongsToUser(Long userId) {
        return (root, query, criteriaBuilder) ->
                userId == null ? null : criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Test> completed(Boolean completed) {
        return (root, query, criteriaBuilder) -> {
            if (completed == null) {
                return null; // No filter applied
            }
            return completed ? criteriaBuilder.isNotNull(root.get("completedAt")) : criteriaBuilder.isNull(root.get("completedAt"));
        };
    }
}
