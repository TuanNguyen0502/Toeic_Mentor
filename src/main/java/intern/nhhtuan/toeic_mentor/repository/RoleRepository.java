package intern.nhhtuan.toeic_mentor.repository;

import intern.nhhtuan.toeic_mentor.entity.ERole;
import intern.nhhtuan.toeic_mentor.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole eRole);
}
