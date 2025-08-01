package intern.nhhtuan.toeic_mentor.repository;

import intern.nhhtuan.toeic_mentor.entity.Goal;
import intern.nhhtuan.toeic_mentor.entity.enums.EGoalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByUser_EmailAndGoalDate(String userEmail, LocalDate goalDate);

    List<Goal> findByUser_EmailOrderByGoalDateDesc(String userEmail);

    List<Goal> findAllByType(EGoalType type);

    List<Goal> findAllByTypeAndGoalDate(EGoalType type, LocalDate goalDate);
}
