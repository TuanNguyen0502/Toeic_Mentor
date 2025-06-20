package intern.nhhtuan.toeic_mentor.repository;

import intern.nhhtuan.toeic_mentor.entity.TestPart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestPartRepository extends JpaRepository<TestPart,Long> {
    int countByPart_Id(Long partId);

    List<TestPart> findByPart_Id(Long partId);

    List<TestPart> findByTest_Id(Long testId);
}
