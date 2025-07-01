package intern.nhhtuan.toeic_mentor.service.implement;

import intern.nhhtuan.toeic_mentor.dto.response.ReportResponse;
import intern.nhhtuan.toeic_mentor.entity.Report;
import intern.nhhtuan.toeic_mentor.entity.enums.EReportStatus;
import intern.nhhtuan.toeic_mentor.repository.ReportRepository;
import intern.nhhtuan.toeic_mentor.service.interfaces.IReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements IReportService {

    private final ReportRepository reportRepository;

    @Override
    public Page<ReportResponse> getReportsByStatus(String status, Pageable pageable) {
        if (status == null || status.isEmpty()) {
            return reportRepository.findAll(pageable)
                    .map(this::mapToResponse);
        } else {
            try {
                EReportStatus reportStatus = EReportStatus.valueOf(status.toUpperCase());
                return reportRepository.findByStatusOrderByCreatedAtDesc(reportStatus, pageable)
                        .map(this::mapToResponse);
            } catch (IllegalArgumentException e) {
                return Page.empty(pageable);
            }
        }
    }

    @Override
    public ReportResponse getReportById(Long id) {
        Report report = reportRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Report not found with id: " + id));
        return mapToResponse(report);
    }

    private ReportResponse mapToResponse(Report report) {
        ReportResponse response = new ReportResponse();
        response.setId(report.getId());
        response.setEmail(report.getUser().getEmail());
        response.setQuestion_id(report.getQuestion().getId());
        response.setCategory(report.getCategory().name());
        response.setStatus(report.getStatus().name());
        response.setCreate_at(report.getCreatedAt().toString());
        return response;
    }
}
