package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.response.ReportResponse;
import intern.nhhtuan.toeic_mentor.entity.enums.EReportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IReportService {
    Page<ReportResponse> getReportsByStatus(String status, Pageable pageable);
    ReportResponse getReportById(Long id);
}
