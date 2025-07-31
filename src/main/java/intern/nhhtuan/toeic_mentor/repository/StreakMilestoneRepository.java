package intern.nhhtuan.toeic_mentor.repository;

import intern.nhhtuan.toeic_mentor.entity.StreakMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreakMilestoneRepository extends JpaRepository<StreakMilestone, Long> {
    boolean existsByDayTarget(Integer dayTarget);

    StreakMilestone findByDayTarget(Integer dayTarget);
}
