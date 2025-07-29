package intern.nhhtuan.toeic_mentor.repository;

import intern.nhhtuan.toeic_mentor.entity.StreakHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StreakHistoryRepository extends JpaRepository<StreakHistory, Long> {
    List<StreakHistory> findByUser_Email(String userEmail);
}
