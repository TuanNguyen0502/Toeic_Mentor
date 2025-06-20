package intern.nhhtuan.toeic_mentor.repository;

import intern.nhhtuan.toeic_mentor.entity.Part;
import intern.nhhtuan.toeic_mentor.entity.enums.EPart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, Long> {
    Part findByName(EPart name);
}
