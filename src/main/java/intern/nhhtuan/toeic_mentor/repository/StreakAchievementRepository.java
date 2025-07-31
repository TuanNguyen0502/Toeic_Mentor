package intern.nhhtuan.toeic_mentor.repository;

import intern.nhhtuan.toeic_mentor.entity.StreakAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StreakAchievementRepository extends JpaRepository<StreakAchievement, Long> {
    List<StreakAchievement> findByUser_Email(String userEmail);

    boolean existsByUser_EmailAndStreakMilestone_DayTarget(String userEmail, Integer dayTarget);
}
