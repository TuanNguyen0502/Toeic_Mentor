package intern.nhhtuan.toeic_mentor.repository;

import intern.nhhtuan.toeic_mentor.entity.Report;
import intern.nhhtuan.toeic_mentor.entity.enums.EReportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Page<Report> findByStatus(EReportStatus status, Pageable pageable);
    Page<Report> findAll(Pageable pageable);
}
