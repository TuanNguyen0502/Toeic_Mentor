package intern.nhhtuan.toeic_mentor.repository;

import intern.nhhtuan.toeic_mentor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
