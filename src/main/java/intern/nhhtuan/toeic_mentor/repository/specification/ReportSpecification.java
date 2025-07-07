package intern.nhhtuan.toeic_mentor.repository.specification;

import intern.nhhtuan.toeic_mentor.entity.Report;
import intern.nhhtuan.toeic_mentor.entity.enums.EReportStatus;
import intern.nhhtuan.toeic_mentor.entity.enums.EReportType;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportSpecification {

    public static Specification<Report> withFilters(Map<String, String> filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filters != null && !filters.isEmpty()) {
                // Filter by status
                if (filters.containsKey("status") && filters.get("status") != null && !filters.get("status").isEmpty()) {
                    try {
                        EReportStatus status = EReportStatus.valueOf(filters.get("status").toUpperCase());
                        predicates.add(criteriaBuilder.equal(root.get("status"), status));
                    } catch (IllegalArgumentException e) {
                        // Invalid status value, ignore this filter
                    }
                }

                // Filter by category
                if (filters.containsKey("category") && filters.get("category") != null && !filters.get("category").isEmpty()) {
                    try {
                        EReportType category = EReportType.valueOf(filters.get("category").toUpperCase());
                        predicates.add(criteriaBuilder.equal(root.get("category"), category));
                    } catch (IllegalArgumentException e) {
                        // Invalid category value, ignore this filter
                    }
                }

                // Filter by email (search in user email)
                if (filters.containsKey("email") && filters.get("email") != null && !filters.get("email").isEmpty()) {
                    Join<Report, Object> userJoin = root.join("user");
                    predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(userJoin.get("email")),
                        "%" + filters.get("email").toLowerCase() + "%"
                    ));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
} 