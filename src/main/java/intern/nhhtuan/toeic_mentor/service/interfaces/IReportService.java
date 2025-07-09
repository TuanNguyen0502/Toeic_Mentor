package intern.nhhtuan.toeic_mentor.service.interfaces;

import intern.nhhtuan.toeic_mentor.dto.ReportDetailDTO;
import intern.nhhtuan.toeic_mentor.dto.response.ReportResponse;
import intern.nhhtuan.toeic_mentor.dto.request.ReportFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import intern.nhhtuan.toeic_mentor.dto.request.ReportRequest;

import java.util.Map;

public interface IReportService {
    Page<ReportResponse> getReportsByStatus(String status, Pageable pageable);
    Page<ReportResponse> getReportsWithFilters(Map<String, String> filters, Pageable pageable);
    ReportDetailDTO getReportDetail(Long id);
    boolean saveReport(String email, ReportRequest request);
    void updateReport(ReportDetailDTO reportDetailDTO);

}
