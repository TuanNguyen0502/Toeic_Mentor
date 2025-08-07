package intern.nhhtuan.toeic_mentor.repository;

import intern.nhhtuan.toeic_mentor.entity.StreakAchievement;
import intern.nhhtuan.toeic_mentor.entity.StreakMilestone;
import intern.nhhtuan.toeic_mentor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StreakAchievementRepository extends JpaRepository<StreakAchievement, Long> {
    List<StreakAchievement> findByUser_Email(String userEmail);

    boolean existsByUser_EmailAndStreakMilestone_DayTarget(String userEmail, Integer dayTarget);

    @Query("""
                SELECT sa
                FROM StreakAchievement sa
                JOIN StudyStreak st ON sa.user.id = st.user.id
                WHERE sa.streakMilestone = :milestone
                  AND st.maxStreak < :target
            """)
    List<StreakAchievement> findAchievementsToRevoke(
            @Param("milestone") StreakMilestone milestone,
            @Param("target") int target);

    @Query("SELECT sa.user FROM StreakAchievement sa WHERE sa.streakMilestone.id = :milestoneId")
    List<User> findUsersByStreakMilestoneId(@Param("milestoneId") Long milestoneId);
}
