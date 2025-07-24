package intern.nhhtuan.toeic_mentor.repository;

import intern.nhhtuan.toeic_mentor.entity.ChatbotRating;
import intern.nhhtuan.toeic_mentor.entity.enums.EChatbotRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatbotRatingRepository extends JpaRepository<ChatbotRating, Long> {
    Page<ChatbotRating> findAll(Specification<ChatbotRating> spec, Pageable pageable);

    int countByRating(EChatbotRating rating);
}
