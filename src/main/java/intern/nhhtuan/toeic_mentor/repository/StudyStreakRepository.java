package intern.nhhtuan.toeic_mentor.repository;

import intern.nhhtuan.toeic_mentor.entity.StudyStreak;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudyStreakRepository extends JpaRepository<StudyStreak, Long> {
    @Query("""
            SELECT s.currentStreak
            FROM StudyStreak s JOIN User u ON s.user.id = u.id
            WHERE u.email = :email
            """)
    int getCurrentStreak(@Param("email") String email);

    Optional<StudyStreak> findByUser_Email(String userEmail);

    @Query("SELECT ss FROM StudyStreak ss WHERE ss.lastStudyDate IS NULL OR ss.lastStudyDate < :yesterday")
    List<StudyStreak> findAllOutdatedStreaks(@Param("yesterday") LocalDateTime yesterday);

    @Query("SELECT ss.lastStudyDate FROM StudyStreak ss WHERE ss.user.email = :email")
    Optional<LocalDateTime> findLastStudyDateByEmail(@Param("email") String email);
}
