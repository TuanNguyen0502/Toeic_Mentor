package intern.nhhtuan.toeic_mentor.repository;

import intern.nhhtuan.toeic_mentor.entity.ChatbotFeedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatbotFeedbackRepository extends JpaRepository<ChatbotFeedback, Long> {
    Page<ChatbotFeedback> findAll(Specification<ChatbotFeedback> spec, Pageable pageable);
}
