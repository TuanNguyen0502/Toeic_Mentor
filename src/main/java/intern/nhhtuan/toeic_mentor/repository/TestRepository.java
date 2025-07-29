package intern.nhhtuan.toeic_mentor.repository;

import intern.nhhtuan.toeic_mentor.entity.Test;
import intern.nhhtuan.toeic_mentor.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TestRepository extends JpaRepository<Test, Long> {
    @Query("SELECT t FROM Test t WHERE t.user.email = :email ORDER BY t.createdAt DESC")
    List<Test> findRecentTestsByUserEmail(@Param("email") String email, Pageable pageable);
    List<Test> findAllByUser(User user);
}
