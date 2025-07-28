package intern.nhhtuan.toeic_mentor.repository;

import intern.nhhtuan.toeic_mentor.entity.User;
import intern.nhhtuan.toeic_mentor.entity.UserStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStatisticRepository extends JpaRepository<UserStatistic, Long> {
    UserStatistic findTopByUserOrderByCreatedAtDesc(User user);

    List<UserStatistic> findAllByUser(User user);
}
